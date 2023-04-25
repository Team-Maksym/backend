package starlight.backend.proof;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import starlight.backend.exception.PageNotFoundException;
import starlight.backend.proof.model.entity.ProofEntity;
import starlight.backend.proof.model.response.ProofInfo;
import starlight.backend.proof.model.response.ProofPagePagination;
import starlight.backend.proof.service.impl.ProofServiceImpl;
import org.springframework.data.domain.Page;
import starlight.backend.talent.model.response.TalentPagePagination;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = ProofController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProofControllerTest {
    @MockBean
    private ProofServiceImpl service;
    @MockBean
    private ProofMapper mapperTalent;
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
}
