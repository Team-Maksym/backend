package starlight.backend.talent;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import starlight.backend.talent.model.response.TalentPagePagination;
import starlight.backend.talent.service.impl.TalentServiceImpl;
import starlight.backend.user.model.entity.UserEntity;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TalentController.class)
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
        List<UserEntity> users = Arrays.asList(new UserEntity(), new UserEntity());
        Page<UserEntity> pageRequest = new PageImpl<>(users);
        TalentPagePagination expectedPagination = mapperTalent.toTalentPagePagination(pageRequest);
        // When
        when(service.talentPagination(page, size)).thenReturn(expectedPagination);
        //Give
        mockMvc.perform(get("/api/v1/talents")
                        .param("page", "0")
                        .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", hasSize(1)))
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