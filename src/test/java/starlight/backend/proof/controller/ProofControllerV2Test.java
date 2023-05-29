//package starlight.backend.proof.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.web.server.ResponseStatusException;
//import starlight.backend.proof.model.enums.Status;
//import starlight.backend.proof.model.request.ProofAddWithSkillsRequest;
//import starlight.backend.proof.model.response.*;
//import starlight.backend.proof.service.ProofServiceInterface;
//import starlight.backend.skill.model.response.SkillWithCategory;
//
//import java.net.URI;
//import java.time.Instant;
//import java.util.Collections;
//import java.util.LinkedList;
//import java.util.List;
//
//import static org.hamcrest.Matchers.hasSize;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(controllers = ProofControllerV2.class)
//class ProofControllerV2Test {
//    @MockBean
//    private ProofServiceInterface proofService;
//    @MockBean
//    private Authentication auth;
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper mapper;
//
//    @DisplayName("JUnit test for getTalentProofs method which throw exception")
//    @Test
//    void getTalentProofs_ShouldThrowUnauthorizedException() throws Exception {
//        // Given
//        long talentId = 1L;
//        int page = 10;
//        int size = 5;
//        boolean sort = true;
//        String status = "ALL";
//
//        when(proofService.getTalentAllProofsWithKudoses(null, talentId, page, size, sort, status))
//                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED));
//
//        // When / Then
//        mockMvc.perform(get("/api/v2/talents/{talent-id}/proofs", talentId)
//                        .param("page", String.valueOf(page))
//                        .param("size", String.valueOf(size))
//                        .param("sort", String.valueOf(sort))
//                        .param("status", status)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isUnauthorized());
//    }
//
//    @DisplayName("JUnit test for getTalentProofs method")
//    @Test
//    void getTalentAllProofsWithKudos() throws Exception {
//        // Given
//        long talentId = 1L;
//        int page = 0;
//        int size = 5;
//        boolean sort = true;
//        String status = "ALL";
//
//        SponsorOnProofShortInfo sponsor = SponsorOnProofShortInfo.builder()
//                .sponsorName("Sample Sponsor")
//                .countKudos(10)
//                .sponsorAvatarUrl("https://example.com/avatar.jpg")
//                .build();
//
//        ProofFullInfoWithKudoses proofInfo = ProofFullInfoWithKudoses.builder()
//                .sponsorOnProofShortInfoList(new LinkedList<>(Collections.singletonList(sponsor)))
//                .dateCreated(Instant.now())
//                .dateLastUpdated(Instant.now())
//                .description("Sample Description")
//                .id(1L)
//                .link("https://example.com/proof")
//                .status(Status.PUBLISHED)
//                .title("Sample Title")
//                .build();
//
//        ProofPagePagination expectedResponse = ProofPagePagination.builder()
//                .total(size)
//                .data(Collections.nCopies(size, proofInfo))
//                .build();
//
//        when(proofService.getTalentAllProofsWithKudoses(any(Authentication.class), eq(talentId), eq(page), eq(size), eq(sort), eq(status)))
//                .thenReturn(expectedResponse);
//
//        // When / Then
//        mockMvc.perform(get("/api/v2/talents/{talent-id}/proofs", talentId)
//                        .with(user("user").roles("TALENT"))
//                        .param("page", String.valueOf(page))
//                        .param("size", String.valueOf(size))
//                        .param("sort", String.valueOf(sort))
//                        .param("status", status))
//                .andExpect(status().isOk());/*
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.total").value(size))
//                .andExpect(jsonPath("$.data").isArray())
//                .andExpect(jsonPath("$.data", hasSize(size)))
//                .andExpect(jsonPath("$.data[0].sponsorOnProofShortInfoList[0].sponsorName").value("Sample Sponsor"))
//                .andExpect(jsonPath("$.data[0].description").value("Sample Description"))
//                .andExpect(jsonPath("$.data[0].status").value("PUBLISHED"))
//                .andExpect(jsonPath("$.data[0].link").value("https://example.com/proof"))
//                .andExpect(jsonPath("$.data[0].title").value("Sample Title"));*/
//    }
//
//    @DisplayName("JUnit test for getTalentProofs with Skills method")
//    @Test
//    public void testGetTalentProofs() throws Exception {
//        long talentId = 1;
//        int page = 0;
//        int size = 5;
//        boolean sort = true;
//        String status = "ALL";
//
//        SkillWithCategory skill = SkillWithCategory.builder()
//                .skill("Sample Skill")
//                .skillId(1L)
//                .build();
//
//        ProofInfoWithSkills proofInfoWithSkills = ProofInfoWithSkills.builder()
//                .dateCreated(Instant.now())
//                .description("Sample Description")
//                .skillWithCategoryList(List.of(skill))
//                .id(1L)
//                .status(Status.PUBLISHED)
//                .title("Sample Title")
//                .build();
//
//        ProofPagePaginationWithSkills expectedResponse = ProofPagePaginationWithSkills.builder()
//                .data(List.of(proofInfoWithSkills))
//                .total(1)
//                .build();
//
//        when(proofService.getTalentAllProofsWithSkills(any(Authentication.class), eq(talentId), eq(page), eq(size), eq(sort), eq(status)))
//                .thenReturn(expectedResponse);
//
//        mockMvc.perform(get("/api/v2/talents/{talent-id}/proofs", talentId)
//                        .with(user("user").roles("TALENT"))
//                        .param("page", String.valueOf(page))
//                        .param("size", String.valueOf(size))
//                        .param("sort", String.valueOf(sort))
//                        .param("status", status))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.data").isArray())
//                .andExpect(jsonPath("$.data", hasSize(1)))
//                .andExpect(jsonPath("$.data[0].id").value(1))
//                .andExpect(jsonPath("$.data[0].title").value("Sample Title"))
//                .andExpect(jsonPath("$.data[0].description").value("Sample Description"))
//                .andExpect(jsonPath("$.data[0].dateCreated").exists())
//                .andExpect(jsonPath("$.data[0].status").value("PUBLISHED"))
//                .andExpect(jsonPath("$.data[0].skillWithCategoryList").isArray())
//                .andExpect(jsonPath("$.data[0].skillWithCategoryList[0].skill").value("Sample Skill"))
//                .andExpect(jsonPath("$.data[0].skillWithCategoryList[0].skillId").value(1));
//    }
//
//    @DisplayName("JUnit test for getFullProof method")
//    @Test
//    public void testGetFullProof() throws Exception {
//        long proofId = 1;
//
//        SkillWithCategory skill = SkillWithCategory.builder()
//                .skill("Sample Skill")
//                .skillId(1L)
//                .build();
//
//        ProofFullInfoWithSkills expectedResponse = ProofFullInfoWithSkills.builder()
//                .dateCreated(Instant.now())
//                .dateLastUpdated(Instant.now())
//                .title("Sample Title")
//                .description("Sample Description")
//                .link("https://example.com")
//                .id(proofId)
//                .skillWithCategoryList(List.of(skill))
//                .build();
//
//        when(proofService.getProofFullInfoWithSkills(any(Authentication.class), eq(proofId)))
//                .thenReturn(expectedResponse);
//
//        mockMvc.perform(get("/api/v2/proofs/{proof-id}", proofId)
//                        .with(user("user").roles("TALENT")))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(proofId))
//                .andExpect(jsonPath("$.title").value("Sample Title"))
//                .andExpect(jsonPath("$.description").value("Sample Description"))
//                .andExpect(jsonPath("$.link").value("https://example.com"))
//                .andExpect(jsonPath("$.dateCreated").exists())
//                .andExpect(jsonPath("$.dateLastUpdated").exists())
//                .andExpect(jsonPath("$.skillWithCategoryList").isArray())
//                .andExpect(jsonPath("$.skillWithCategoryList[0].skill").value("Sample Skill"))
//                .andExpect(jsonPath("$.skillWithCategoryList[0].skillId").value(1));
//    }
//
//    @DisplayName("JUnit test for add info to proof method")
//    @Test
//    void addProofFullInfo() throws Exception {
//        //Given
//        int talentId = 1;
//        URI location = new URI("proof");
//        ProofAddWithSkillsRequest proofAddWithSkillsRequest = ProofAddWithSkillsRequest.builder().build();
//
//        when(proofService.getLocationForAddProofWithSkill(eq(talentId), eq(proofAddWithSkillsRequest), any(Authentication.class)))
//                .thenReturn(ResponseEntity.created(location).build());
//        //When //Then
//        mockMvc.perform(post("/api/v2/talents/{talent-id}/proofs", talentId)
//                        .with(user("user").roles("TALENT"))
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(mapper.writeValueAsString(proofAddWithSkillsRequest)))
//                .andDo(print())
//                .andExpect(status().isCreated());
//    }
//}
