package starlight.backend.email;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import starlight.backend.email.model.ChangePassword;
import starlight.backend.email.model.Email;
import starlight.backend.email.service.EmailService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class EmailControllerTest {
    @Autowired
    private EmailController emailController;

    @MockBean
    private EmailService emailService;

    @MockBean
    private Authentication auth;

    @Test
    public void testSendMail() {
        Email email = Email.builder()
                .text("This is a test email")
                .subject("Test email")
                .build();
        long sponsorId = 123;

        emailController.sendMail(email, sponsorId, auth);

        verify(emailService, times(1)).sendMail(eq(email), eq(sponsorId), eq(auth));
    }

    @Test
    public void testForgotPassword() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String email = "test@test.com";

        emailController.forgotPassword(request, email);

        verify(emailService, times(1)).forgotPassword(eq(request), eq(email));
    }

    @Test
    public void testRecoveryPassword() {
        String token = "test_token";
        ChangePassword changePassword = ChangePassword.builder()
                .password("new_password")
                .build();

        emailController.recoveryPassword(token, changePassword);

        verify(emailService, times(1)).recoveryPassword(eq(token), eq(changePassword));
    }

    @Test
    public void testRecoveryAccount() throws Exception {
        UUID uuid = UUID.randomUUID();
        ResponseEntity<String> expectedResponse = ResponseEntity
                .ok("Account recovered, please sign in again");

        doNothing().when(emailService).recoverySponsorAccount(uuid);

        ResponseEntity<String> actualResponse = emailController.recoveryAccount(uuid.toString());

        assertEquals(expectedResponse, actualResponse);
        verify(emailService, times(1)).recoverySponsorAccount(eq((uuid)));
    }
}