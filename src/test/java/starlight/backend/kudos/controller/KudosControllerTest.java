package starlight.backend.kudos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import starlight.backend.kudos.model.entity.KudosEntity;
import starlight.backend.kudos.model.response.KudosOnProof;
import starlight.backend.kudos.service.KudosServiceInterface;

import java.time.Instant;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = KudosController.class)
@AutoConfigureMockMvc(addFilters = false)
class KudosControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private Authentication auth;
    @MockBean
    private KudosServiceInterface kudosService;

    @DisplayName("JUnit test for getKudosOnProofShouldReturnKudosOnProof")
    @Test
    @Order(1)
    @WithMockUser(username = "user1", roles = {"SPONSOR"})
    public void getKudosOnProofShouldReturnKudosOnProof() throws Exception {
        //Given
        int proofId = 1;
        KudosOnProof kudosOnProof = KudosOnProof.builder()
                .kudosOnProof(1)
                .kudosFromMe(1)
                .isKudosed(true)
                .build();
        given(kudosService.getKudosOnProof(proofId, auth)).willReturn(kudosOnProof);

        // When // Then
        mockMvc.perform(get("/api/v1/proofs/{proof-id}/kudos", proofId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.kudos_on_proof").value(kudosOnProof.kudosOnProof()))
                .andExpect(jsonPath("$.kudos_from_me").value(kudosOnProof.kudosFromMe()))
                .andExpect(jsonPath("$.is_kudosed").value(kudosOnProof.isKudosed()))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @DisplayName("JUnit test for addKudosShouldReturnKudosEntity")
    @Test
    @Order(1)
    @WithMockUser(username = "user1", roles = {"SPONSOR"})
    public void addKudosShouldReturnKudosEntity() throws Exception {
        //Given
        int proofId = 1;
        int kudos = 20;
        KudosEntity kudosEntity = KudosEntity.builder()
                .kudosId(1L)
                .followerId(1L)
                .countKudos(20)
                .updateData(Instant.MIN)
                .createData(Instant.MAX)
                .build();
        when(kudosService.addKudosOnProof(proofId, kudos, auth)).thenReturn(kudosEntity);

        // When // Then
        mockMvc.perform(post("/api/v1/proofs/{proof-id}/kudos", proofId)
                        .param("kudos", String.valueOf(kudos))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.follower_id").value(kudosEntity.getFollowerId()))
                .andExpect(jsonPath("$.count_kudos").value(kudosEntity.getCountKudos()))
                .andExpect(jsonPath("$.update_data").value(kudosEntity.getUpdateData()))
                .andExpect(jsonPath("$.create_data").value(kudosEntity.getCreateData()))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @DisplayName("JUnit test for add Kudos Should Return Bad Request When Kudos Parameter Is Not Provided")
    @Test
    public void addKudosShouldReturnBadRequestWhenKudosParameterIsNotProvided() throws Exception {
        //Given
        int proofId = 1;
        when(kudosService.getKudosOnProof(proofId, null))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        // When // Then
        mockMvc.perform(post("/api/v1/proofs/{proof-id}/kudos", proofId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("JUnit test for add Kudos Should Return Unauthorized When Kudos Parameter Is Not Authenticated")
    @Test
    @Order(1)
    @WithMockUser(username = "user1", roles = {"SPONSOR"})
    public void addKudosShouldReturnUnauthorizedWhenKudosParameterIsNotProvided() throws Exception {
        //Given
        int proofId = 1;

        // When // Then
        mockMvc.perform(post("/api/v1/proofs/{proof-id}/kudos", proofId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("JUnit test for add Kudos Should Return Unauthorized When User Is Not Authenticated")
    @Test
    public void addKudosShouldReturnUnauthorizedWhenUserIsNotAuthenticated() throws Exception {
        //Given
        int proofId = 1;
        when(kudosService.addKudosOnProof(proofId, 1, null))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        // When // Then
        mockMvc.perform(post("/api/v1/proofs/{proof-id}/kudos", proofId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("kudos", "1"))
                .andExpect(status().isUnauthorized());
    }
}