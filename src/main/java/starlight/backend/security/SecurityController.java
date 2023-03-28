package starlight.backend.security;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import starlight.backend.security.service.SecurityServiceInterface;
import starlight.backend.security.model.request.NewUser;
import starlight.backend.security.model.response.SessionInfo;

@AllArgsConstructor
@RestController
public class SecurityController {
    private SecurityServiceInterface service;

    @PostMapping("/talents/login")
    public SessionInfo login(Authentication authentication) {
        return service.loginInfo(authentication.getName());
    }

    @PostMapping("/talents")
    @ResponseStatus(HttpStatus.CREATED)
    public SessionInfo register(@Valid @RequestBody NewUser newUser) {
        return service.register(newUser);
    }
}
