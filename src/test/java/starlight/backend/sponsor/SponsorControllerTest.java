package starlight.backend.sponsor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import starlight.backend.email.model.EmailProps;
import starlight.backend.sponsor.model.request.SponsorUpdateRequest;
import starlight.backend.sponsor.model.response.SponsorFullInfo;
import starlight.backend.sponsor.model.response.SponsorKudosInfo;
import starlight.backend.sponsor.service.SponsorServiceInterface;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(SponsorController.class)
class SponsorControllerTest {
    @MockBean
    private SponsorServiceInterface sponsorService;

    @MockBean
    private EmailProps emailProps;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private Authentication auth;

    @Autowired
    private MockMvc mockMvc;

    @Test
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

    @Test
    public void sponsorFullInfo_ReturnsSponsorFullInfo() throws Exception {
        long sponsorId = 1L;
        SponsorFullInfo expected = SponsorFullInfo.builder()
                .fullName("John Doe")
                .fullName("john.doe@example.com")
                .build();

        given(sponsorService.getSponsorFullInfo(sponsorId, auth)).willReturn(expected);

        mockMvc.perform(get("/api/v1/sponsors/" + sponsorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sponsorId").value(sponsorId))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    public void updateSponsorFullInfo_ReturnsSponsorFullInfo() throws Exception {
        long sponsorId = 1L;

        SponsorUpdateRequest updateRequest = SponsorUpdateRequest.builder()
                .fullName("John Doe")
                .fullName("john.doe@example.com")
                .build();
        SponsorFullInfo expected = SponsorFullInfo.builder()
                .fullName("John Doe")
                .fullName("john.doe@example.com")
                .build();

        given(sponsorService.updateSponsorProfile(sponsorId, updateRequest, auth)).willReturn(expected);

        mockMvc.perform(patch("/api/v1/sponsors/" + sponsorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sponsorId").value(sponsorId))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    public void delete_ReturnsOk() throws Exception {
        long sponsorId = 1L;

        mockMvc.perform(delete("/api/v1/sponsors/" + sponsorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Dear sponsor,")));

        verify(sponsorService, times(1)).deleteSponsor(sponsorId, auth);
    }
}