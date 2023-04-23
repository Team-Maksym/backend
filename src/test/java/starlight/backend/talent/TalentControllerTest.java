package starlight.backend.talent;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import starlight.backend.exception.PageNotFoundException;
import starlight.backend.exception.TalentNotFoundException;
import starlight.backend.talent.model.request.TalentUpdateRequest;
import starlight.backend.talent.model.response.TalentFullInfo;
import starlight.backend.talent.model.response.TalentPagePagination;
import starlight.backend.talent.model.response.TalentProfile;
import starlight.backend.talent.service.impl.TalentServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = TalentController.class)
@AutoConfigureMockMvc(addFilters = false)
class TalentControllerTest {
    @MockBean
    private TalentServiceImpl service;
    @MockBean
    private MapperTalent mapper;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private Authentication auth;
    @Autowired
    private MockMvc mockMvc;

    @DisplayName("JUnit test for pagination (talents) method")
    @Test
    void pagination() throws Exception {
        //Given
        int page = 0;
        int size = 10;
        TalentProfile talentProfile = TalentProfile.builder().build();
        List<TalentProfile> talentProfiles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            talentProfiles.add(talentProfile);
        }
        TalentPagePagination talentPagePagination = TalentPagePagination.builder()
                .data(talentProfiles)
                .total(10)
                .build();
        when(service.talentPagination(page, size)).thenReturn(talentPagePagination);

        //When //Then
        mockMvc.perform(get("/api/v1/talents")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", hasSize(10)))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.total", is(10)))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @DisplayName("JUnit test for pagination (talents) method which throw exception")
    @Test
    void pagination_WithInvalidPageNumber_ShouldThrowPageNotFoundException() throws Exception {
        //Given
        int page = 10;
        int size = 10;
        when(service.talentPagination(page, size)).thenThrow(new PageNotFoundException(page));

        //When //Then
        mockMvc.perform(get("/api/v1/talents")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("No such page " + page)))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @DisplayName("JUnit test for get full info about talent method")
    @Test
    @Order(1)
    @WithMockUser(username = "user1", roles = {"TALENT"})
    void searchTalentById() throws Exception {
        // Given
        int talentId = 1;
        TalentFullInfo expectedTalent = TalentFullInfo.builder()
                .fullName("John Doe")
                .email("john.doe@example.com")
                .birthday(LocalDate.of(1995, 1, 1))
                .avatar("https://example.com/new-avatar.jpg")
                .education("Master's Degree")
                .experience("5 years")
                .positions(List.of("Senior Software Engineer"))
                .build();
        when(service.talentFullInfo(talentId)).thenReturn(expectedTalent);

        // When //Then
        mockMvc.perform(get("/api/v1/talents/{talent-id}", talentId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.full_name").value(expectedTalent.fullName()))
                .andExpect(jsonPath("$.email").value(expectedTalent.email()))
                .andExpect(jsonPath("$.birthday").value(String.valueOf(expectedTalent.birthday())))
                .andExpect(jsonPath("$.avatar").value(expectedTalent.avatar()))
                .andExpect(jsonPath("$.education").value(expectedTalent.education()))
                .andExpect(jsonPath("$.experience").value(expectedTalent.experience()))
                .andExpect(jsonPath("$.positions").isArray());
    }
    @DisplayName("JUnit test for get full info about talent method which throw exception Unauthorized")
    @Test
    void searchTalentById_WithInvalidId_ShouldThrowUnauthorizedException()  throws Exception {
        // Given
        int talentId = 1;
        when(service.talentFullInfo(talentId))
                .thenThrow(new TalentNotFoundException(talentId));

        // When //Then
        mockMvc.perform(get("/api/v1/talents/{talent-id}", talentId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Talent not found by id " + talentId)))
                .andExpect(jsonPath("$").isNotEmpty());
    }
    @DisplayName("JUnit test for update info about talent method")
    @Test
    @Order(1)
    @WithMockUser(username = "user1", roles = {"TALENT"})
    void updateTalentFullInfo() throws Exception {
        // Given
        int talentId = 1;
        TalentUpdateRequest updateRequest = TalentUpdateRequest.builder()
                .fullName("John Doe")
                .password("Secret123")
                .birthday(LocalDate.of(1995, 1, 1))
                .avatar("https://example.com/new-avatar.jpg")
                .education("Master's Degree")
                .experience("5 years")
                .positions(List.of("Senior Software Engineer"))
                .build();

        TalentFullInfo expectedTalent = TalentFullInfo.builder()
                .fullName(updateRequest.fullName())
                .birthday(updateRequest.birthday())
                .avatar(updateRequest.avatar())
                .education(updateRequest.education())
                .experience(updateRequest.experience())
                .positions(updateRequest.positions())
                .build();

        when(service.updateTalentProfile(talentId, updateRequest, auth)).thenReturn(expectedTalent);

        // When // Then
        mockMvc.perform(patch("/api/v1/talents/{talent-id}", talentId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @DisplayName("JUnit test for update info about talent method which throw exception Unauthorized")
    @Test
    void updateTalentFullInfo_WithInvalidId_ShouldThrowUnauthorizedException() throws Exception {
        // Given
        int talentId = 1;
        TalentUpdateRequest updateRequest = TalentUpdateRequest.builder().build();
        when(service.updateTalentProfile(talentId, updateRequest, null))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you cannot change another talent"));

        //When //Then
        mockMvc.perform(patch("/api/v1/talents/{talent-id}", talentId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Delete talent profile successfully")
    @Test
    @Order(1)
    @WithMockUser(username = "user1", roles = {"TALENT"})
    void deleteTalent() throws Exception {
        // Given
        int talentId = 1;
        doNothing().when(service).deleteTalentProfile(talentId, auth);

        // When // Then
        mockMvc.perform(delete("/api/v1/talents/{talent-id}", talentId))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("JUnit test for delete talent method which throw exception Unauthorized")
    @Test
    void deleteTalent_WithInvalidId_ShouldThrowUnauthorizedException() throws Exception {
        // Given
        int talentId = 1;
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you cannot delete another talent"))
                .when(service).deleteTalentProfile(talentId, null);

        // When // Then
        mockMvc.perform(delete("/api/v1/talents/{talent-id}", talentId))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}