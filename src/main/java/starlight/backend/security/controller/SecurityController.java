package starlight.backend.security.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import starlight.backend.security.service.UserServiceInterface;
import starlight.backend.talent.model.request.NewUser;
import starlight.backend.talent.model.response.SessionInfo;

@AllArgsConstructor
@RestController
public class SecurityController {
    private UserServiceInterface service;

    @PostMapping("/talents/login")
    public SessionInfo login(Authentication authentication) {
        return service.loginInfo(authentication);
    }

    @PostMapping("/talents")
    @ResponseStatus(HttpStatus.CREATED)
    public SessionInfo register(@Valid @RequestBody NewUser newUser) {
        return service.register(newUser);
    }
}
