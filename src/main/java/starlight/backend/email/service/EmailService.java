package starlight.backend.email.service;

import jakarta.servlet.http.HttpServletRequest;
import starlight.backend.email.model.ChangePasswordRequest;
import starlight.backend.email.model.Email;

public interface EmailService {

    void sendMail(Email email);

    void sendSimpleMessage(String to, String subject, String text);

    void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment);

    void forgotPassword(HttpServletRequest request, String email);

    void recoveryPassword(String token, ChangePasswordRequest changePasswordRequest);
}
