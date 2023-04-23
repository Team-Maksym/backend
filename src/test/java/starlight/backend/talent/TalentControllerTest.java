package starlight.backend.talent;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import starlight.backend.talent.model.response.TalentPagePagination;
import starlight.backend.talent.model.response.TalentProfile;
import starlight.backend.talent.service.impl.TalentServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = TalentController.class)
@AutoConfigureMockMvc(addFilters = false)
class TalentControllerTest {
    @MockBean
    private TalentServiceImpl service;

    @MockBean
    private MapperTalent mapperTalent;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void pagination() throws Exception {
        //Given
        int page = 0;
        int size = 10;
        TalentProfile talentProfile = TalentProfile.builder().build();
        List<TalentProfile> talentProfiles =new ArrayList<>();
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
                        .param("page", "0")
                        .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", hasSize(10)))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void searchTalentById() {
    }

    @Test
    void updateTalentFullInfo() {
    }

    @Test
    void deleteTalent() {
    }
}