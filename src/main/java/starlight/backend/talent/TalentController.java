package starlight.backend.talent;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import starlight.backend.exception.TalentAlreadyOccupiedException;
import starlight.backend.talent.model.request.TalentUpdateRequest;
import starlight.backend.talent.model.response.TalentFullInfo;
import starlight.backend.talent.model.response.TalentPagePagination;
import starlight.backend.talent.service.TalentServiceInterface;

import java.util.Optional;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/api/v1")
@Tag(name = "Talent", description = "Talent API")
public class TalentController {
    private TalentServiceInterface talentService;

    @Operation(
            summary = "Get all talents",
            description = "Get list of all talents. The response is list of talent objects with fields 'id','full_name', 'position' and 'avatar'."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = TalentPagePagination.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = Exception.class
                            )
                    )
            )
    })
    @GetMapping("/talents")
    public TalentPagePagination pagination(@RequestParam(defaultValue = "0") @Min(0) int page,
                                           @RequestParam(defaultValue = "10") @Positive int size) {
        return talentService.talentPagination(page, size);
    }

    @Operation(
            summary = "Get talent by id",
            description = "Get a talent from id. The response is talent object with fields 'full_name', 'email', 'birthday', 'avatar', 'education', 'experience', 'positions'.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = TalentFullInfo.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = Exception.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = Exception.class
                            )
                    )
            )
    })
    @PreAuthorize("hasRole('TALENT')")
    @GetMapping("/talents/{talent-id}")
    public Optional<TalentFullInfo> searchTalentById(@PathVariable("talent-id") long talentId) {
        return talentService.talentFullInfo(talentId);
    }

    @Operation(summary = "Update talent by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = TalentFullInfo.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = Exception.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = Exception.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = Exception.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = Exception.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = Exception.class
                            )
                    )
            )
    })
    @PreAuthorize("hasRole('TALENT')")
    @PatchMapping("/talents/{talent-id}")
    public TalentFullInfo updateTalentFullInfo(@PathVariable("talent-id") long talentId,
                                               @RequestBody TalentUpdateRequest talentUpdateRequest,
                                               Authentication auth) {
        return talentService.updateTalentProfile(talentId, talentUpdateRequest, auth);
    }

    @Operation(summary = "Delete talent by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = TalentFullInfo.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = Exception.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = Exception.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = TalentAlreadyOccupiedException.class
                            )
                    )
            )
    })
    @PreAuthorize("hasRole('TALENT')")
    @DeleteMapping("/talents/{talent-id}")
    public void deleteTalent(@PathVariable("talent-id") long talentId,
                             Authentication auth) {
        talentService.deleteTalentProfile(talentId, auth);
    }
}