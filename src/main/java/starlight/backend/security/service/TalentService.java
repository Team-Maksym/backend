package starlight.backend.security.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import starlight.backend.security.model.UserDetailsImpl;
import starlight.backend.talent.model.entity.UserEntity;
import starlight.backend.talent.model.request.NewUser;
import starlight.backend.talent.repository.UserRepository;

import java.time.Instant;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.MINUTES;

@AllArgsConstructor
@Service
@Transactional
public class TalentService {
    private final JwtEncoder jwtEncoder;
    private UserRepository repository;

    private PasswordEncoder passwordEncoder;

    public UserEntity register(NewUser newUser) {
        if (Boolean.FALSE.equals(repository.existsByEmail(newUser.email())))
            return repository.save(UserEntity.builder()
                    .fullName(newUser.full_name())
                    .email(newUser.email())
                    .password(passwordEncoder.encode(newUser.password()))
                    .build());
        else
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Email '" + newUser.email() + "' is already occupied");
    }

    public String getJWTToken(UserDetailsImpl authentication) {
        var now = Instant.now();
        var claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(30, MINUTES))
                .subject(authentication.getUsername())
                .claim("scope", createScope(authentication))
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private String createScope(UserDetailsImpl authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
    }
}
