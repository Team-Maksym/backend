package starlight.backend.proof.model.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ProofPagePagination(
        long totalProofs,
        List<ProofInfo> proofs
) {
}
