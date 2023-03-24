package starlight.backend.security.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;
import starlight.backend.security.mapper.SecurityMapper;
import starlight.backend.security.service.TalentService;
import starlight.backend.talent.model.request.NewTalent;
import starlight.backend.talent.model.response.CreatedTalent;

import java.time.Instant;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.MINUTES;

@AllArgsConstructor
@RestController
@Slf4j
public class SecurityController {
    TalentService service;
    SecurityMapper mapper;
    final JwtEncoder jwtEncoder;

    @PreAuthorize("hasRole('TALENT')")
    @GetMapping ("/talents/login")
    public String login(Authentication authentication) {
        log.info("=== POST /login === auth.name = {}", authentication.getName());
        log.info("=== POST /login === auth = {}", authentication);
        var now = Instant.now();
        var claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(30, MINUTES))
                .subject(authentication.getName())
                .claim("scope", createScope(authentication))
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
    private String createScope(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
    }


//
//        return "Hello, " + authentication.getName() + ", u in system now!";
//    }

    @PostMapping("/talents")
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedTalent register(@Valid @RequestBody NewTalent newTalent) {
        return mapper.toCreatedUser(service.register(newTalent));
    }
}
