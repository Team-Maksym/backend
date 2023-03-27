package starlight.backend.security.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import starlight.backend.security.mapper.SecurityMapper;
import starlight.backend.security.service.TalentService;
import starlight.backend.talent.model.request.NewTalent;
import starlight.backend.talent.model.response.CreatedTalent;

@AllArgsConstructor
@RestController
@Slf4j
public class SecurityController {
    TalentService service;
    SecurityMapper mapper;

    @PostMapping("/talents")
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedTalent register(@Valid @RequestBody NewTalent newTalent) {
        return mapper.toCreatedUser(service.register(newTalent));
    }

    @PostMapping("/talents/login")
    public String login(Authentication authentication) {
        return "Hello, " + authentication.getName() + ", u in system now!";
    }
}
