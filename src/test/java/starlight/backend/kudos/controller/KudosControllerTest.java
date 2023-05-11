package starlight.backend.kudos.controller;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import starlight.backend.kudos.KudosService;
import starlight.backend.kudos.model.entity.KudosEntity;
import starlight.backend.kudos.model.response.KudosOnProof;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = KudosController.class)
@AutoConfigureMockMvc(addFilters = false)
class KudosControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private Authentication auth;
    @MockBean
    private KudosService kudosService;

    @Test
    @Order(1)
    @WithMockUser(username = "user1", roles = {"SPONSOR"})
    public void getKudosOnProofShouldReturnKudosOnProof() throws Exception {
        KudosOnProof kudosOnProof = KudosOnProof.builder().build();
        when(kudosService.getKudosOnProof(1, auth)).thenReturn(kudosOnProof);

        mockMvc.perform(get("/api/v1/proofs/1/kudos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
                /*.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").value(kudosOnProof));

                 */
    }

    @Test
    @Order(1)
    @WithMockUser(username = "user1", roles = {"SPONSOR"})
    public void addKudosShouldReturnKudosEntity() throws Exception {
        KudosEntity kudosEntity = new KudosEntity();
        when(kudosService.addKudosOnProof(1, 1, auth))
                .thenReturn(kudosEntity);

        mockMvc.perform(post("/api/v1/proofs/1/kudos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("kudos", "1"))
                .andDo(print())
                .andExpect(status().isCreated());
               /* .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").value(kudosEntity));

                */
    }

    @Test
    @Order(1)
    @WithMockUser(username = "user1", roles = {"SPONSOR"})
    public void addKudosShouldReturnBadRequestWhenKudosParameterIsNotProvided() throws Exception {
        mockMvc.perform(post("/api/v1/proofs/1/kudos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addKudosShouldReturnUnauthorizedWhenUserIsNotAuthenticated() throws Exception {
        when(kudosService.addKudosOnProof(1, 1, null))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        mockMvc.perform(post("/api/v1/proofs/1/kudos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("kudos", "1"))
                .andExpect(status().isUnauthorized());
    }
}