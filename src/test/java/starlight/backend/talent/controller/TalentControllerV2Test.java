package starlight.backend.talent.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import starlight.backend.proof.model.enums.Status;
import starlight.backend.proof.model.response.ProofFullInfoWithSkills;
import starlight.backend.proof.model.response.ProofListWithSkills;
import starlight.backend.skill.model.response.SkillWithCategory;
import starlight.backend.skill.service.SkillServiceInterface;
import starlight.backend.talent.model.response.TalentWithSkills;
import starlight.backend.talent.service.TalentServiceInterface;

import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class TalentControllerV2Test {

    private MockMvc mockMvc;

    @Mock
    private SkillServiceInterface skillService;

    @Mock
    private TalentServiceInterface talentService;

    @Before
    public void setup() {
        TalentControllerV2 talentController = new TalentControllerV2(skillService, talentService);
        mockMvc = MockMvcBuilders.standaloneSetup(talentController).build();
    }

    @Test
    public void testGetSkillsOfTalent() throws Exception {
        long talentId = 1;
        Authentication auth = new TestingAuthenticationToken("test@gmail.com", "Secret123",
                List.of(new SimpleGrantedAuthority("ROLE_TALENT")));
        TalentWithSkills talentWithSkills = new TalentWithSkills(
                talentId,
                "John Doe",
                "Developer",
                "avatarurl.com",
                Arrays.asList(
                        new SkillWithCategory(1, "Skill 1", "Category 1"),
                        new SkillWithCategory(2, "Skill 2", "Category 2")
                )
        );

        when(skillService.getListSkillsOfTalent(talentId, auth)).thenReturn(talentWithSkills);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v2/talents/{talent-id}", talentId)
                .principal(auth);

        ResultActions resultActions = mockMvc.perform(requestBuilder);

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is((int) talentId)))
                .andExpect(jsonPath("$.fullName", is("John Doe")))
                .andExpect(jsonPath("$.position", is("Developer")))
                .andExpect(jsonPath("$.skill", hasSize(2)))
                .andExpect(jsonPath("$.skill[0].skill", is("Skill 1")))
                .andExpect(jsonPath("$.skill[0].category", is("Category 1")))
                .andExpect(jsonPath("$.skill[1].skill", is("Skill 2")))
                .andExpect(jsonPath("$.skill[1].category", is("Category 2")));

        verify(skillService, times(1)).getListSkillsOfTalent(talentId, auth);
    }

    @Test
    public void testGetProofsOfSkill() throws Exception {
        long talentId = 1;
        long skillId = 2;
        String status = "ALL";
        Authentication auth = new TestingAuthenticationToken("test@gmail.com", "Secret123",
                List.of(new SimpleGrantedAuthority("ROLE_TALENT")));

        ProofFullInfoWithSkills proof1 = new ProofFullInfoWithSkills(
                1,
                "Proof 1",
                "Description 1",
                "http://example.com/proof1",
                Instant.parse("2023-05-20T12:00:00Z"),
                Instant.parse("2023-05-20T13:00:00Z"),
                Status.PUBLISHED,
                new LinkedList<>(Arrays.asList(
                        new SkillWithCategory(1, "Skill 1", "Category 1"),
                        new SkillWithCategory(2, "Skill 2", "Category 2")
                ))
        );

        ProofFullInfoWithSkills proof2 = new ProofFullInfoWithSkills(
                2,
                "Proof 2",
                "Description 2",
                "http://example.com/proof2",
                Instant.parse("2023-05-21T09:00:00Z"),
                Instant.parse("2023-05-21T10:00:00Z"),
                Status.PUBLISHED,
                new LinkedList<>(Arrays.asList(
                        new SkillWithCategory(2, "Skill 2", "Category 2"),
                        new SkillWithCategory(3, "Skill 3", "Category 3")
                ))
        );
        ProofListWithSkills proofListWithSkills = ProofListWithSkills.builder()
                .data(Arrays.asList(proof1, proof2))
                .build();

        when(skillService.getListProofsOfSkill(talentId, skillId, status, auth))
                .thenReturn(proofListWithSkills);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v2/talents/{talent-id}/skills/{skill-id}/proofs", talentId, skillId)
                .principal(auth)
                .accept(MediaType.APPLICATION_JSON);

        ResultActions resultActions = mockMvc.perform(requestBuilder);

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].title", is("Proof 1")))
                .andExpect(jsonPath("$.data[0].description", is("Description 1")))
                .andExpect(jsonPath("$.data[0].link", is("http://example.com/proof1")))
                .andExpect(jsonPath("$.data[0].status", is("PUBLISHED")))
                .andExpect(jsonPath("$.data[0].skillWithCategoryList", hasSize(2)))
                .andExpect(jsonPath("$.data[0].skillWithCategoryList[0].skillId", is(1)))
                .andExpect(jsonPath("$.data[0].skillWithCategoryList[0].skill", is("Skill 1")))
                .andExpect(jsonPath("$.data[0].skillWithCategoryList[0].category", is("Category 1")))
                .andExpect(jsonPath("$.data[0].skillWithCategoryList[1].skillId", is(2)))
                .andExpect(jsonPath("$.data[0].skillWithCategoryList[1].skill", is("Skill 2")))
                .andExpect(jsonPath("$.data[0].skillWithCategoryList[1].category", is("Category 2")))
                .andExpect(jsonPath("$.data[1].id", is(2)))
                .andExpect(jsonPath("$.data[1].title", is("Proof 2")))
                .andExpect(jsonPath("$.data[1].description", is("Description 2")))
                .andExpect(jsonPath("$.data[1].link", is("http://example.com/proof2")))
                .andExpect(jsonPath("$.data[1].status", is("PUBLISHED")))
                .andExpect(jsonPath("$.data[1].skillWithCategoryList", hasSize(2)))
                .andExpect(jsonPath("$.data[1].skillWithCategoryList[0].skillId", is(2)))
                .andExpect(jsonPath("$.data[1].skillWithCategoryList[0].skill", is("Skill 2")))
                .andExpect(jsonPath("$.data[1].skillWithCategoryList[0].category", is("Category 2")))
                .andExpect(jsonPath("$.data[1].skillWithCategoryList[1].skillId", is(3)))
                .andExpect(jsonPath("$.data[1].skillWithCategoryList[1].skill", is("Skill 3")))
                .andExpect(jsonPath("$.data[1].skillWithCategoryList[1].category", is("Category 3")));
        verify(skillService, times(1)).getListProofsOfSkill(talentId, skillId, status, auth);
    }
}
