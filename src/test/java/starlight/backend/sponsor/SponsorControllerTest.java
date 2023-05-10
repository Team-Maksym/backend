package starlight.backend.sponsor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import starlight.backend.email.model.EmailProps;
import starlight.backend.proof.controller.ProofControllerV2;
import starlight.backend.sponsor.model.response.SponsorKudosInfo;
import starlight.backend.sponsor.service.SponsorServiceInterface;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(SponsorController.class)
@AutoConfigureMockMvc(addFilters = false)
class SponsorControllerTest {
    @MockBean
    private SponsorServiceInterface sponsorService;

    @MockBean
    private EmailProps emailProps;

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
    void sponsorFullInfo() {
    }

    @Test
    void updateSponsorFullInfo() {
    }

    @Test
    void delete() {
    }
}