package starlight.backend.security.service.impl;


import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import starlight.backend.exception.TalentAlreadyOccupiedException;
import starlight.backend.security.SecurityMapper;
import starlight.backend.security.model.UserDetailsImpl;
import starlight.backend.security.service.SecurityServiceInterface;
import starlight.backend.user.model.entity.UserEntity;
import starlight.backend.security.model.request.NewUser;
import starlight.backend.security.model.response.SessionInfo;
import starlight.backend.user.repository.UserRepository;

import java.time.Instant;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.MINUTES;

@AllArgsConstructor
@Service
@Transactional
public class SecurityServiceImpl implements SecurityServiceInterface {
    private final JwtEncoder jwtEncoder;
    private UserRepository repository;
    private SecurityMapper mapper;

    private PasswordEncoder passwordEncoder;

    @Override
    public SessionInfo loginInfo(String userName) {
        var user = repository.findByEmail(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName + " not found user by email"));
        var token = getJWTToken(mapper.toUserDetailsImpl(user));
        return mapper.toSessionInfo(token);
    }

    @Override
    @Transactional(readOnly = true)
    public SessionInfo register(NewUser newUser) {
        var user = saveNewUser(newUser);
        var token = getJWTToken(mapper.toUserDetailsImpl(user));
        return mapper.toSessionInfo(token);
    }

    private UserEntity saveNewUser(NewUser newUser) {
        if (repository.existsByEmail(newUser.email())) {
            throw new TalentAlreadyOccupiedException(newUser.email());
        }
        return repository.save(UserEntity.builder()
                .fullName(newUser.fullName())
                .email(newUser.email())
                .password(passwordEncoder.encode(newUser.password()))
                .build());
    }

    private String getUserIdByEmail(String email){
       var user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + " not found user by email"));
       return user.getUserId().toString();
    }

    @Transactional(readOnly = true)
    String getJWTToken(UserDetailsImpl authentication) {
        var now = Instant.now();
        var claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(60, MINUTES))
                .subject(getUserIdByEmail(authentication.getUsername()))
                .claim("scope", createScope(authentication))
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Transactional(readOnly = true)
    String createScope(UserDetailsImpl authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
    }
}
