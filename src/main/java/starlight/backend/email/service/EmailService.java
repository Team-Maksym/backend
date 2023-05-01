package starlight.backend.email.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import starlight.backend.email.model.ChangePassword;
import starlight.backend.email.model.Email;

public interface EmailService {

    void sendMail(Email email);

    void sendSimpleMessage(String to, String subject, String text);

    void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment);

    void forgotPassword(HttpServletRequest request, Authentication auth);

    void recoveryPassword(Authentication auth, ChangePassword changePassword);
}
