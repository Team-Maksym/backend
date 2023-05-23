package starlight.backend.talent.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import starlight.backend.proof.model.response.ProofListWithSkills;
import starlight.backend.proof.model.response.ProofWithSkills;
import starlight.backend.skill.model.response.SkillListWithPagination;
import starlight.backend.skill.service.SkillServiceInterface;
import starlight.backend.talent.model.response.TalentWithSkills;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/api/v2")
@Tag(name = "Talent v2", description = "Talent API v2 (with skills")
@Slf4j
public class TalentControllerV2 {
    private SkillServiceInterface serviceService;

    @Operation(
            summary = "Get talent with skills",
            description = "On this you can see the skills of a specific talent."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = SkillListWithPagination.class
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })

    @GetMapping("/talents/{talent-id}")
    public TalentWithSkills getSkillsOfTalent(@PathVariable("talent-id") long talentId,
                                              Authentication auth){

        log.info("@GetMapping(\"/talents/{talent-id}/skills\")");
        return serviceService.getListSkillsOfTalent(talentId, auth);
    }


    @Operation(
            summary = "Get proofs of skill",
            description = "On this you can see the proofs of a specific skill."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = ProofWithSkills.class
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/talents/{talent-id}/skills/{skill-id}/proofs")
    public ProofListWithSkills getProofsOfSkill(@PathVariable("talent-id") long talentId,
                                                @PathVariable("skill-id") long skillId,
                                                Authentication auth){

        log.info("@GetMapping(\"/talents/{talent-id}/skills/{skills-id}/proofs\")");
        return serviceService.getListProofsOfSkill(talentId, skillId, auth);
    }
}
