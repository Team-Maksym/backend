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
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import starlight.backend.exception.PageNotFoundException;
import starlight.backend.proof.model.request.ProofAddRequest;
import starlight.backend.proof.model.response.ProofPagePagination;
import starlight.backend.proof.service.ProofServiceInterface;
import starlight.backend.security.service.SecurityServiceInterface;


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

    @Operation(
            summary = "Add proof in status draft",
            description = "Adding a proof for a specific talent, issuing a link in Location to a page with a full description of the proof."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Created",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = ResponseEntity.class
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
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = Exception.class
                            )
                    )
            )
    })
    @PreAuthorize("hasRole('TALENT')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/talents/{talent-id}/proofs")
    public ResponseEntity<?> addProofFullInfo(@PathVariable("talent-id") long talentId,
                                              @RequestBody ProofAddRequest proofAddRequest,
                                              Authentication auth) {
        if (!securityService.checkingLoggedAndTokenValid(talentId, auth)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return proofService.getLocation(talentId, proofAddRequest);
    }
}