package starlight.backend.proof.controller;

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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import starlight.backend.exception.PageNotFoundException;
import starlight.backend.proof.model.enums.Status;
import starlight.backend.proof.model.request.ProofAddRequest;
import starlight.backend.proof.model.response.ProofFullInfo;
import starlight.backend.proof.model.response.ProofInfo;
import starlight.backend.proof.model.response.ProofPagePagination;
import starlight.backend.proof.service.impl.ProofServiceImpl;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = ProofControllerV1.class)
@AutoConfigureMockMvc(addFilters = false)
class ProofControllerV1Test {
    @MockBean
    private ProofServiceImpl service;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private Authentication auth;
    @Autowired
    private MockMvc mockMvc;

    @DisplayName("JUnit test for pagination (proof) method")
    @Test
    void pagination() throws Exception {
        //Given
        int page = 0;
        int size = 5;
        boolean sort = true;
        ProofInfo proofInfo = ProofInfo.builder().build();
        List<ProofInfo> proofs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            proofs.add(proofInfo);
        }
        ProofPagePagination proofPagePagination = ProofPagePagination.builder()
                .data(proofs)
                .total(5)
                .build();
        when(service.proofsPagination(page, size, sort)).thenReturn(proofPagePagination);

        //When //Then
        mockMvc.perform(get("/api/v1/proofs")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sort", String.valueOf(sort))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", hasSize(5)))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.total", is(5)))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @DisplayName("JUnit test for pagination (proof) method which throw exception")
    @Test
    void pagination_WithInvalidPageNumber_ShouldThrowPageNotFoundException() throws Exception {
        //Given
        int page = 10;
        int size = 10;
        boolean sort = true;
        when(service.proofsPagination(page, size, sort)).thenThrow(new PageNotFoundException(page));

        //When //Then
        mockMvc.perform(get("/api/v1/proofs")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("No such page " + page)))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @DisplayName("JUnit test for add info to proof method")
    @Test
    @Order(1)
    @WithMockUser(username = "user1", roles = {"TALENT"})
    void addProofFullInfo() throws Exception {
        //Given
        int talentId = 1;
        URI location = new URI("proof");
        ProofAddRequest addRequest = ProofAddRequest.builder()
                .title("TITLE")
                .description("DESCRIPTION")
                .link("https://example.com/new-avatar.jpg")
                .build();

        when(service.getLocation(talentId, addRequest, auth)).thenReturn(ResponseEntity.created(location).build());
        //When //Then
        mockMvc.perform(post("/api/v1/talents/{talent-id}/proofs", talentId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(addRequest)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("Delete talent proof successfully")
    @Test
    @Order(1)
    @WithMockUser(username = "user1", roles = {"TALENT"})
    void deleteTalentProof() throws Exception {
        // Given
        int talentId = 1;
        int proofId = 1;
        doNothing().when(service).deleteProof(talentId, proofId, auth);

        // When // Then
        mockMvc.perform(delete("/api/v1/talents/{talent-id}/proofs/{proof-id}", talentId, proofId))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("JUnit test for delete talent proofs method which throw exception Unauthorized")
    @Test
    @Order(1)
    @WithMockUser(username = "user1", roles = {"TALENT"})
    void deleteTalentProof_WithInvalidId_ShouldThrowUnauthorizedException() throws Exception {
        // Given
        int talentId = 1;
        int proofId = 1;
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you cannot delete another talent"))
                .when(service).deleteProof(talentId, proofId, null);

        // When // Then
        mockMvc.perform(delete("/api/v1/talents/{talent-id}/proofs/{proof-id}", talentId, proofId))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("JUnit test for get list of talent proofs method")
    @Test
    @WithMockUser(username = "user1", roles = {"TALENT"})
    void getTalentProofs() throws Exception {
        // Given
        long talentId = 1L;
        int page = 0;
        int size = 5;
        boolean sort = true;
        String status = "ALL";
        ProofInfo proofInfo = ProofInfo.builder()
                .title("Sample Title")
                .description("Sample Description")
                .dateCreated(Instant.now())
                .id(1L)
                .status(Status.PUBLISHED)
                .build();
        List<ProofInfo> proofs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            proofs.add(proofInfo);
        }
        ProofPagePagination proofPagePagination = ProofPagePagination.builder()
                .data(proofs)
                .total(5)
                .build();

        given(service.getTalentAllProofs(any(Authentication.class), eq(talentId), eq(page), eq(size), eq(sort), eq(status)))
                .willReturn(proofPagePagination);

        // When / Then
        mockMvc.perform(get("/api/v1/talents/{talent-id}/proofs", talentId)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sort", String.valueOf(sort))
                        .param("status", status)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());/*
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", hasSize(5)))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.total", is(5)))
                .andExpect(jsonPath("$").isNotEmpty());*/
    }

    @DisplayName("JUnit test for get full info about talent proof method")
    @Test
    @WithMockUser(username = "user1", roles = {"TALENT"})
    void getFullProof() throws Exception {
        // Given
        int proofId = 1;
        ProofFullInfo expectedTalentProof = ProofFullInfo.builder()
                .title("TITLE")
                .description("DESCRIPTION")
                .link("https://example.com/new-avatar.jpg")
                .dateCreated(Instant.parse("2004-09-14T10:30:12.00Z"))
                .dateLastUpdated(Instant.parse("2005-09-14T10:30:12.00Z"))
                .status(Status.PUBLISHED)
                .build();

        given(service.getProofFullInfo(any(Authentication.class), eq(proofId)))
                .willReturn(expectedTalentProof);
        given(service.getProofFullInfo(auth, proofId))
                .willReturn(expectedTalentProof);

        // When //Then
        mockMvc.perform(get("/api/v1/proofs/{proof-id}", proofId))
                .andExpect(status().isOk());/*
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value(expectedTalentProof.title()))
                .andExpect(jsonPath("$.description").value(expectedTalentProof.description()))
                .andExpect(jsonPath("$.link").value(expectedTalentProof.link()))
                .andExpect(jsonPath("$.date_сreated").value(String.valueOf(expectedTalentProof.dateCreated())))
                .andExpect(jsonPath("$.date_last_updated").value(String.valueOf(expectedTalentProof.dateLastUpdated())))
                .andExpect(jsonPath("$.status").value(String.valueOf(expectedTalentProof.status())));
   */
    }
}
