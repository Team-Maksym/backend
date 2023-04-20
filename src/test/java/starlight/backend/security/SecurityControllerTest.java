package starlight.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import starlight.backend.security.model.request.NewUser;
import starlight.backend.security.model.response.SessionInfo;
import starlight.backend.security.service.SecurityServiceInterface;
import starlight.backend.user.model.entity.UserEntity;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SecurityController.class)
class SecurityControllerTest {
    @MockBean
    SecurityServiceInterface service;
    @Autowired
    MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

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
    void login() throws Exception {
        //Given
        SessionInfo session = SessionInfo.builder().build();
        //When
        when(service.loginInfo(user.getEmail())).thenReturn(session);
        //Then
        mockMvc.perform(
                        post("/talents")
                                .content(objectMapper.writeValueAsString(user.getEmail()))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void register() throws Exception {
        //Given
        SessionInfo session = SessionInfo.builder().build();
        //When
        when(service.register(any(NewUser.class))).thenReturn(session);
        //Then
        mockMvc.perform(
                        post("/talents")
                                .content(objectMapper.writeValueAsString(newUser))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }
}