package starlight.backend.proof;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import starlight.backend.proof.model.request.ProofAddRequest;
import starlight.backend.proof.model.response.ProofPagePagination;
import starlight.backend.proof.service.ProofServiceInterface;

import java.net.URI;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/api/v1")
public class ProofController {
    private ProofServiceInterface proofService;

    @GetMapping("/proofs")
    public ProofPagePagination pagination(@RequestParam(defaultValue = "0") @Min(0) int page,
                                          @RequestParam(defaultValue = "10") @Positive int size,
                                          @RequestParam(defaultValue = "true") Boolean sortDate) {
        return proofService.proofsPagination(page, size, sortDate);
    }

    @PreAuthorize("hasRole('TALENT')")
    @PostMapping("/talents/{talent-id}/proofs")
    public ResponseEntity<?> addTalentFullInfo(@PathVariable("talent-id") long talentId,
                                               @RequestBody ProofAddRequest proofAddRequest,
                                               Authentication auth) {
        var proofId = proofService.validationProof(talentId, proofAddRequest, auth);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{proof-id}")
                .buildAndExpand(proofId)
                .toUri();
        return ResponseEntity.created(location).build();
    }

}
