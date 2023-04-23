package starlight.backend.security.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import starlight.backend.exception.TalentAlreadyOccupiedException;
import starlight.backend.security.MapperSecurity;
import starlight.backend.security.model.UserDetailsImpl;
import starlight.backend.security.model.request.NewUser;
import starlight.backend.security.model.response.SessionInfo;
import starlight.backend.user.model.entity.UserEntity;
import starlight.backend.user.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class SecurityServiceImplTest {
    @MockBean
    private UserRepository repository;
    @MockBean
    private MapperSecurity mapperSecurity;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private JwtEncoder jwtEncoder;
    @InjectMocks
    private SecurityServiceImpl securityService;
    private NewUser newUser;
    @MockBean
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
        when(repository.existsByEmail(user.getEmail())).thenReturn(false);
        given(repository.save(user)).willReturn(user);
        when(mapperSecurity.toUserDetailsImpl(any(UserEntity.class))).thenReturn(any(UserDetailsImpl.class));

        //When
        SessionInfo sessionInfo = securityService.register(newUser);

        //Then
        assertThat(sessionInfo).isNotNull();
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
        SessionInfo sessionInfo = securityService.loginInfo(user.getEmail());

        //Then
        assertThat(sessionInfo).isNotNull();
    }

    @DisplayName("JUnit test for login method which throws exception")
    @Test
    void testLoginInfoNotFound() {
        //Given
        String email = "test@example.com";
        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        //When/Then
        assertThrows(UsernameNotFoundException.class, () -> {
            securityService.loginInfo(email);
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