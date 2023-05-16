package starlight.backend.skill;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import starlight.backend.skill.model.request.AddSkill;
import starlight.backend.skill.model.response.ProofWithSkills;
import starlight.backend.skill.model.response.SkillList;
import starlight.backend.skill.model.response.SkillListWithPagination;
import starlight.backend.skill.service.SkillServiceInterface;

@Slf4j
@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/api/v1")
@Tag(name = "Skill", description = "Skill API")
public class SkillController {
    private SkillServiceInterface serviceService;

    @Operation(
            summary = "Get list of skill",
            description = "Get all skills."
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
    @GetMapping("/skills")
    public SkillListWithPagination listWithFilter(@RequestParam(defaultValue = "0") @Min(0) int skip,
                                                  @RequestParam(defaultValue = "30") @Positive int limit,
                                                  @RequestParam String filter) {
        log.info("@GetMapping(\"/skills\")");
        return serviceService.getListSkillWithFiltration(filter, skip, limit);
    }
}
