package starlight.backend.email.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import starlight.backend.email.model.ChangePasswordRequest;
import starlight.backend.email.model.Email;
import starlight.backend.email.service.EmailService;
import starlight.backend.user.model.entity.UserEntity;
import starlight.backend.user.repository.UserRepository;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {
    @Value("${spring.mail.username}")
    private String email;

    private JavaMailSender emailSender;

    private UserRepository userRepository;

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
        message.setFrom(email);
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

            helper.setFrom(email);
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
    public void forgotPassword(HttpServletRequest request, String email) {
        var user = userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));

        String token = UUID.randomUUID().toString();
        createPasswordResetTokenForUser(user, token);
        sendSimpleMessage(email, "Password recovery", constructResetTokenEmail(getAppUrl(request), token));
    }

    @Override
    public void recoveryPassword(String token, ChangePasswordRequest changePasswordRequest) {
        if (!userRepository.existsByActivationCode(token)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid token");
        }
        UserEntity user = userRepository.findByActivationCode(token);
        user.setPassword(passwordEncoder.encode(changePasswordRequest.password()));
        user.setActivationCode(null);
        userRepository.save(user);
    }

    private void createPasswordResetTokenForUser(UserEntity user, String token) {
        user.setActivationCode(token);
        user.setExpiryDate(calculateExpiryDate(10));
        userRepository.save(user);
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    private String constructResetTokenEmail(String appUrl, String token) {
        return String.format("You received this email about a password recovery request. The link will be invalid after 10 minutes.\n" +
                        "If you haven't done so, please ignore this email and change your password on your account!\n%s\n",
                appUrl + "/recovery-password?token=" + token);
    }

    private String getAppUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getHeader("host");
    }
}