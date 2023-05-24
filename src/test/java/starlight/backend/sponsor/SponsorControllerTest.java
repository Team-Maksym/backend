package starlight.backend.sponsor;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import starlight.backend.email.model.EmailProps;
import starlight.backend.sponsor.model.request.SponsorUpdateRequest;
import starlight.backend.sponsor.model.response.SponsorFullInfo;
import starlight.backend.sponsor.model.response.SponsorKudosInfo;
import starlight.backend.sponsor.service.SponsorServiceInterface;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = SponsorController.class)
@AutoConfigureMockMvc(addFilters = false)
class SponsorControllerTest {
    @MockBean
    private SponsorServiceInterface sponsorService;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private Authentication auth;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmailProps emailProps;
    @BeforeEach
    void setUp() {
        when(emailProps.username()).thenReturn("test@example.com");
    }
    @DisplayName("JUnit test for get Unusable Kudos for Sponsor")
    @Test
    @Order(1)
    @WithMockUser(username = "user1", roles = {"SPONSOR"})
    void getUnusableKudosForSponsor() throws Exception {
        //Given
        long sponsorId = 1L;
        SponsorKudosInfo sponsorKudosInfo = SponsorKudosInfo.builder().build();
        when(sponsorService.getUnusableKudos(sponsorId, auth)).thenReturn(sponsorKudosInfo);

        //When //Then
        mockMvc.perform(get("/api/v1/sponsors/{sponsor-id}/kudos", sponsorId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.unused_kudos", is(0)))
                .andExpect(jsonPath("$.already_marked_kudos", is(0)))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @DisplayName("JUnit test for sponsor FullInfo Returns Sponsor FullInfo")
    @Test
    @Order(1)
    @WithMockUser(username = "user1", roles = {"SPONSOR"})
    void sponsorFullInfo_ReturnsSponsorFullInfo() throws Exception {
        //Given
        long sponsorId = 1L;
        SponsorFullInfo expected = SponsorFullInfo.builder()
                .fullName("John Doe")
                .avatar("https://example.com/new-avatar.jpg")
                .company("Master's Degree")
                .build();

        when(sponsorService.getSponsorFullInfo(sponsorId, auth)).thenReturn(expected);

        // When // Then
        mockMvc.perform(get("/api/v1/sponsors/{sponsor-id}", sponsorId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.full_name").value("John Doe"))
                .andExpect(jsonPath("$.company").value("Master's Degree"))
                .andExpect(jsonPath("$.avatar").value("https://example.com/new-avatar.jpg"));
    }

    @DisplayName("JUnit test for update Sponsor FullInfo Returns Sponsor FullInfo")
    @Test
    @Order(1)
    @WithMockUser(username = "user1", roles = {"SPONSOR"})
    void updateSponsorFullInfo_ReturnsSponsorFullInfo() throws Exception {
        //Given
        long sponsorId = 1L;

        SponsorUpdateRequest updateRequest = SponsorUpdateRequest.builder()
                .fullName("John Doe")
                .fullName("john.doe@example.com")
                .build();
        SponsorFullInfo expected = SponsorFullInfo.builder()
                .fullName("John Doe")
                .fullName("john.doe@example.com")
                .build();

        when(sponsorService.updateSponsorProfile(sponsorId, updateRequest, auth)).thenReturn(expected);

        // When // Then
        mockMvc.perform(patch("/api/v1/sponsors/{sponsor-id}", sponsorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
    }

    @DisplayName("JUnit test for Sponsor delete")
    @Test
    @Order(1)
    @WithMockUser(username = "user1", roles = {"SPONSOR"})
    void delete_ReturnsOk() throws Exception {
        //Given
        long sponsorId = 1L;
        doNothing().when(sponsorService).deleteSponsor(sponsorId, auth);

        // When // Then
        mockMvc.perform(delete("/api/v1/sponsors/{sponsor-id}", sponsorId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Dear sponsor,")));
    }
}
