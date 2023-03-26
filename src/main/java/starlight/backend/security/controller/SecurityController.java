package starlight.backend.security.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import starlight.backend.security.mapper.SecurityMapper;
import starlight.backend.security.model.UserDetailsImpl;
import starlight.backend.security.service.UserServiceInterface;
import starlight.backend.talent.model.entity.UserEntity;
import starlight.backend.talent.model.request.NewUser;
import starlight.backend.talent.model.response.SessionInfo;
import starlight.backend.talent.repository.UserRepository;

@AllArgsConstructor
@RestController
public class SecurityController {
    private UserServiceInterface service;
    private SecurityMapper mapper;
    private UserRepository repository;

    @PostMapping("/talents/login")
    public SessionInfo login(Authentication authentication) {
        var userEntityOptional = repository.findByEmail(authentication.getName());
        UserEntity user = null;
        if (userEntityOptional.isPresent())
            user = userEntityOptional.get();
        assert user != null;
        var token = service.getJWTToken(new UserDetailsImpl(
                user.getEmail(),
                user.getPassword()));
        return mapper.toSessionInfo(user, token);
    }

    @PostMapping("/talents")
    @ResponseStatus(HttpStatus.CREATED)
    public SessionInfo register(@Valid @RequestBody NewUser newUser) {
        var user = service.register(newUser);
        var token = service.getJWTToken(new UserDetailsImpl(
                user.getEmail(),
                user.getPassword()));
        return mapper.toSessionInfo(user, token);
    }
}
