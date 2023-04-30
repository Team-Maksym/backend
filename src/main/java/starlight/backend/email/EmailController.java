package starlight.backend.email;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import starlight.backend.email.model.ChangePasswordRequest;
import starlight.backend.email.model.Email;
import starlight.backend.email.service.EmailService;

@RestController
@AllArgsConstructor
public class EmailController {

    private EmailService emailService;

    @PostMapping("/send")
    public void sendMail(@RequestBody Email email) {
        emailService.sendMail(email);
    }

    @PostMapping("/forgot-password")
    public void forgotPassword(HttpServletRequest request, @RequestParam String email) {
        emailService.forgotPassword(request, email);
    }

    @PostMapping("/recovery-password")
    public void recoveryPassword(@RequestParam String token, @RequestBody ChangePasswordRequest changePasswordRequest) {
        emailService.recoveryPassword(token, changePasswordRequest);
    }
}
