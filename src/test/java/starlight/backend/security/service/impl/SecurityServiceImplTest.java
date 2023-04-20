package starlight.backend.security.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import starlight.backend.exception.TalentAlreadyOccupiedException;
import starlight.backend.security.MapperSecurity;
import starlight.backend.security.model.request.NewUser;
import starlight.backend.security.model.response.SessionInfo;
import starlight.backend.user.model.entity.UserEntity;
import starlight.backend.user.repository.UserRepository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SecurityServiceImplTest {
    @Mock
    private UserRepository repository;
    @Mock
    private MapperSecurity mapperSecurity;
    @Mock
    private PasswordEncoder passwordEncoder;
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

    @Test
    void loginInfo() {
    }

    @DisplayName("JUnit test for register new user method")
    @Test
    void register() {
        // Given
        given(repository.findByEmail(newUser.email()))
                .willReturn(Optional.empty());

        given(repository.save(UserEntity.builder()
                .fullName(newUser.fullName())
                .email(newUser.email())
                .password(passwordEncoder.encode(newUser.password()))
                .build()))
                .willReturn(user);

        // When
        SessionInfo sessionInfo = securityService.register(newUser);

        // Then
        assertThat(sessionInfo).isNotNull();
    }

    @DisplayName("JUnit test for register method which throws exception")
    @Test
    void givenExistingEmail_whenRegister_thenThrowsException(){
        // Given
        given(repository.findByEmail(newUser.email()))
                .willReturn(Optional.of(user));

        // When
        assertThrows(TalentAlreadyOccupiedException.class, () -> {
            securityService.register(newUser);
        });

        // Then
        verify(repository, never()).save(any(UserEntity.class));
    }
}