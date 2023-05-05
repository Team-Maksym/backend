package starlight.backend.security.service.impl;
/*
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.context.junit4.SpringRunner;
import starlight.backend.security.MapperSecurity;
import starlight.backend.security.model.UserDetailsImpl;
import starlight.backend.security.model.request.NewUser;
import starlight.backend.security.model.response.SessionInfo;
import starlight.backend.user.model.entity.UserEntity;
import starlight.backend.user.repository.UserRepository;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class SecurityServiceImplTest {
    @MockBean
    private Authentication auth;
    @MockBean
    private UserRepository repository;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private MapperSecurity mapperSecurity;
    @MockBean
    private JwtEncoder jwtEncoder;
    @Autowired
    private SecurityServiceImpl securityService;
    private NewUser newUser;
    private UserEntity user;

    @BeforeEach
    public void setup() {
        user = UserEntity.builder()
                .userId(1L)
                .fullName("Jon Snow")
                .email("myemail@gmail.com")
                .password("Secret123")
                .build();
        newUser = NewUser.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    @DisplayName("JUnit test for register method")
    @Test
    void register() {
        //Given
        UserEntity savedUser = UserEntity.builder()
                .userId(1L)
                .fullName(newUser.fullName())
                .email(newUser.email())
                .password("encoded_password")
                .build();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(180))
                .subject(String.valueOf(user.getUserId()))
                .claim("scope", "ROLE_TALENT")
                .build();
        SessionInfo expectedSessionInfo = SessionInfo.builder().build();
        UserDetailsImpl userDetails = new UserDetailsImpl(newUser.email(), savedUser.getPassword());

        when(repository.existsByEmail(newUser.email())).thenReturn(false);
        when(repository.save(any(UserEntity.class))).thenReturn(savedUser);
        when(securityService.saveNewUser(newUser)).thenReturn(savedUser);
        when(mapperSecurity.toUserDetailsImpl(savedUser)).thenReturn(userDetails);
        when(mapperSecurity.toSessionInfo(expectedSessionInfo.token())).thenReturn(expectedSessionInfo);

        Jwt mockJwt = mock(Jwt.class);
        when(mockJwt.getClaims()).thenReturn(any());
        when(jwtEncoder.encode(JwtEncoderParameters.from(claims)))
                .thenReturn(mockJwt);

        //When
        SessionInfo sessionInfo = securityService.register(newUser);

        //Then
        assertEquals(expectedSessionInfo.token(), sessionInfo.token());
    }

    @DisplayName("JUnit test for login")
    @Test
    void loginInfo() {
        //Given
        UserDetailsImpl userDetails = new UserDetailsImpl(user.getEmail(), user.getPassword());
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(180))
                .subject(String.valueOf(user.getUserId()))
                .claim("scope", "ROLE_TALENT")
                .build();
        SessionInfo expectedSessionInfo = SessionInfo.builder().build();

        when(repository.findByEmail(auth.getName())).thenReturn(Optional.of(user));
        when(mapperSecurity.toUserDetailsImpl(user)).thenReturn(userDetails);
        when(mapperSecurity.toSessionInfo(expectedSessionInfo.token())).thenReturn(expectedSessionInfo);

        Jwt mockJwt = mock(Jwt.class);
        when(mockJwt.getClaims()).thenReturn(any());
        when(jwtEncoder.encode(JwtEncoderParameters.from(claims)))
                .thenReturn(mockJwt);

        //When
        SessionInfo session = securityService.loginInfo(auth);

        //Then
        assertEquals(expectedSessionInfo.token(), session.token());
    }

    @DisplayName("JUnit test for login method which throws exception")
    @Test
    void loginInfoNotFound() {
        //Given
        String email = "test@example.com";
        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        //When/Then
        assertThrows(UsernameNotFoundException.class, () -> {
            securityService.loginInfo(auth);
        });
    }

    @DisplayName("JUnit test for register method which throws exception")
    @Test
    void testRegisterThrowsExceptionIfUserExists() {
        //Given
        when(repository.existsByEmail(user.getEmail())).thenReturn(true);

        //When/Then
        assertThrows(TalentAlreadyOccupiedException.class,
                () -> securityService.register(newUser)
        );
    }
}


 */