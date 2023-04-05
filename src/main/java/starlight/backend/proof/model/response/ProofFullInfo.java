package starlight.backend.proof.model.response;

import lombok.Builder;
import starlight.backend.proof.model.enums.Status;

import java.time.Instant;

@Builder
public record ProofFullInfo(
        long id,
        String title,
        String description,
        String link,
        Instant dateCreated,
        Status status
) {
}
