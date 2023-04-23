package starlight.backend.security.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.context.junit4.SpringRunner;
import starlight.backend.exception.TalentAlreadyOccupiedException;
import starlight.backend.security.MapperSecurity;
import starlight.backend.security.model.UserDetailsImpl;
import starlight.backend.security.model.request.NewUser;
import starlight.backend.security.model.response.SessionInfo;
import starlight.backend.user.model.entity.UserEntity;
import starlight.backend.user.repository.UserRepository;

import java.time.Instant;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.MINUTES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    @InjectMocks
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
        SessionInfo sessionInfo = SessionInfo.builder().build();
        when(repository.existsByEmail(user.getEmail())).thenReturn(false);
        when(repository.save(user)).thenReturn(user);
        when(mapperSecurity.toUserDetailsImpl(any(UserEntity.class))).thenReturn(any(UserDetailsImpl.class));
        var now = Instant.now();
        var claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(180, MINUTES))
                .subject(String.valueOf(user.getUserId()))
                .build();
        when(jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue()).thenReturn(sessionInfo.token());
        //When
        SessionInfo session = securityService.register(newUser);

        //Then
        assertEquals(session,sessionInfo);
    }

    @DisplayName("JUnit test for login")
    @Test
    void loginInfo() {
        //Given
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(mapperSecurity.toUserDetailsImpl(user)).thenReturn(new UserDetailsImpl(
                user.getEmail(),
                user.getPassword()));


        //When
        SessionInfo sessionInfo = securityService.loginInfo(auth);

        //Then
        assertNotNull(sessionInfo);
        assertNotNull(sessionInfo.token());
        assertThat(sessionInfo).isNotNull();
    }

    @DisplayName("JUnit test for login method which throws exception")
    @Test
    void loginInfoNotFound() {
        //Given
        String email = "test@example.com";
        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        //When/Then
        assertThrows(UsernameNotFoundException.class, () -> {
            securityService.loginInfo(null);
        });
    }

    @DisplayName("JUnit test for register method which throws exception")
    @Test
    void testRegisterThrowsExceptionIfUserExists() {
        //Given
        when(repository.existsByEmail(user.getEmail())).thenReturn(true);

        //When/Then
        assertThrows(TalentAlreadyOccupiedException.class, () -> securityService.register(newUser));
    }
}