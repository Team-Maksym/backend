package starlight.backend.email;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import starlight.backend.email.model.ChangePassword;
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
    public void forgotPassword(HttpServletRequest request,
                               @RequestParam String email) {
        emailService.forgotPassword(request, email);
    }

    @PostMapping("/recovery-password")
    @ResponseStatus(HttpStatus.CREATED)
    public void recoveryPassword(@RequestParam String token,
                                 @RequestBody ChangePassword changePassword) {
        emailService.recoveryPassword(token, changePassword);
    }
}
