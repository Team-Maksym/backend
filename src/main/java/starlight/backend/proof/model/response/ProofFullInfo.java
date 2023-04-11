package starlight.backend.proof.model.response;

import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import starlight.backend.proof.model.enums.Status;

import java.time.Instant;

@Builder
public record ProofFullInfo(
        String title,
        String description,
        String link,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        Instant dateCreated,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        Instant dateLastUpdated,
        Status status
) {
}
