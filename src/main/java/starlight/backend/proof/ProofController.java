package starlight.backend.proof;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import starlight.backend.exception.PageNotFoundException;
import starlight.backend.exception.TalentAlreadyOccupiedException;
import starlight.backend.proof.model.response.ProofPagePagination;
import starlight.backend.proof.service.ProofServiceInterface;
import starlight.backend.security.service.SecurityServiceInterface;
import starlight.backend.talent.model.response.TalentFullInfo;


@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/api/v1")
@Tag(name = "Proof", description = "Proof API")
public class ProofController {
    private ProofServiceInterface proofService;
    private SecurityServiceInterface securityService;

    @Operation(
            summary = "Get all proofs",
            description = "Get list of all proofs. The response is list of talent objects with fields 'id','title', 'description' and 'dateCreated'."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = ProofPagePagination.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = PageNotFoundException.class
                            )
                    )
            )
    })
    @GetMapping("/proofs")
    public ProofPagePagination pagination(@RequestParam(defaultValue = "0") @Min(0) int page,
                                          @RequestParam(defaultValue = "5") @Positive int size,
                                          @RequestParam(defaultValue = "true") boolean sort) {
        return proofService.proofsPagination(page, size, sort);
    }

    @Operation(summary = "Delete proof by proof_id and talent_id")
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
    @DeleteMapping("/talents/{talent-id}/proofs/{proof-id}")
    public void deleteTalent(@PathVariable("talent-id") long talentId,
                             @PathVariable("proof-id") long proofId,
                             Authentication auth) {
        if (!securityService.checkingLoggedAndTokenValid(talentId, auth)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        proofService.deleteProof(talentId,proofId);
    }
}