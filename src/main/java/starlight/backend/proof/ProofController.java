package starlight.backend.proof;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import starlight.backend.proof.model.response.ProofPagePagination;
import starlight.backend.proof.service.ProofServiceInterface;

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
}
