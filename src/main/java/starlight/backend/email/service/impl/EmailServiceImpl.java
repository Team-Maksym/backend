package starlight.backend.email.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import starlight.backend.email.model.ChangePassword;
import starlight.backend.email.model.Email;
import starlight.backend.email.model.EmailProps;
import starlight.backend.email.service.EmailService;
import starlight.backend.sponsor.SponsorRepository;
import starlight.backend.sponsor.model.entity.SponsorEntity;

import java.io.File;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {
    private EmailProps emailProps;

    private JavaMailSender emailSender;

    private SponsorRepository sponsorRepository;

    private PasswordEncoder passwordEncoder;

    @Override
    public void sendMail(Email email) {
        if (email.pathToAttachment() == null) {
            sendSimpleMessage(email.to(), email.subject(), email.text());
        } else {
            sendMessageWithAttachment(email.to(), email.subject(), email.text(), email.pathToAttachment());
        }
    }

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailProps.username());
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    @Override
    public void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment) {
        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);

            helper.setFrom(emailProps.username());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
            helper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        emailSender.send(message);
    }

    @Override
    @Transactional
    public void forgotPassword(HttpServletRequest request, String email) {
        var sponsor = sponsorRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));
        String token = UUID.randomUUID().toString();
        createPasswordResetTokenForUser(sponsor, token);
        sendSimpleMessage(email, "Password recovery",
                constructResetTokenEmail(getAppUrl(request), token));
    }

    @Override
    @Transactional
    public void recoveryPassword(String token, ChangePassword changePassword) {
        var sponsor = sponsorRepository.findByActivationCode(token).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));
        if (!sponsorRepository.existsByActivationCode(sponsor.getActivationCode())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid token");
        }
        sponsor.setPassword(passwordEncoder.encode(changePassword.password()));
        sponsor.setActivationCode(null);
        sponsorRepository.save(sponsor);
    }

    @Transactional
    void createPasswordResetTokenForUser(SponsorEntity sponsor, String token) {
        sponsor.setActivationCode(token);
        sponsor.setExpiryDate(calculateExpiryDate(7));
        sponsorRepository.save(sponsor);
    }

    private Instant calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.DAY_OF_WEEK, expiryTimeInMinutes);
        return cal.toInstant();
    }

    private String constructResetTokenEmail(String appUrl, String token) {
        return String.format("You received this email about a password recovery request. " +
                        "The link will be invalid after 7 days.\n" +
                        "If you haven't done so, please ignore this email and change your " +
                        "password on your account!\n%s\n",
                appUrl + "/recovery-password?token=" + token);
    }

    private String getAppUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getHeader("host");
    }
}