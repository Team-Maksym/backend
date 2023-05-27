package starlight.backend.proof.controller;

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
import starlight.backend.proof.ProofMapper;
import starlight.backend.proof.model.entity.ProofEntity;
import starlight.backend.proof.model.response.ProofPagePagination;
import starlight.backend.proof.service.ProofServiceInterface;
import starlight.backend.sponsor.SponsorController;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProofControllerV2.class)
class ProofControllerV2Test {
    @MockBean
    private ProofServiceInterface proofService;
    @MockBean
    private Authentication auth;
    @Autowired
    private MockMvc mockMvc;

    @DisplayName("JUnit test for getTalentProofs method which throw exception")
    @Test
    @Order(1)
    @WithMockUser(username = "user", roles = {"TALENT"})
    void getTalentProofs() throws Exception {
        // Given
        long talentId = 1L;
        int page = 0;
        int size = 5;
        boolean sort = true;
        String status = "ALL";
        ProofEntity proofInfo = new ProofEntity();
        List<ProofEntity> proofs = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            proofs.add(proofInfo);
        }
        ProofPagePagination proofPagePagination = ProofPagePagination.builder()
                .data(proofs)
                .total(5)
                .build();
        given(proofService.getTalentAllProofsWithKudoses(auth, talentId, page, size, sort, status))
                .willReturn(proofPagePagination);
        given(proofService.getTalentAllProofsWithKudoses(any(Authentication.class), eq(talentId), eq(page), eq(size), eq(sort), eq(status)))
                .willReturn(proofPagePagination);

        // When // Then
        mockMvc.perform(get("/api/v2/talents/{talent-id}/proofs", talentId)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sort", String.valueOf(sort))
                        .param("status", status)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());/*\\\
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(5)))
                .andExpect(jsonPath("$").isNotEmpty());*/
    }

    @DisplayName("JUnit test for getTalentProofs method which throw exception")
    @Test
    void getTalentProofs_WithInvalidPageNumber_ShouldThrowPageNotFoundException() throws Exception {
        //Given
        long talentId = 1L;
        int page = 0;
        int size = 5;
        boolean sort = true;
        String status = "ALL";
        when(proofService.getTalentAllProofsWithKudoses(null, talentId, page, size, sort, status))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        //When //Then
        mockMvc.perform(get("/api/v2/talents/{talent-id}/proofs", talentId)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sort", String.valueOf(sort))
                        .param("status", status)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
