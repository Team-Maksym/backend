package starlight.backend.proof.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.Instant;

@Builder
public record ProofInfo(
        long id,
        String title,
        String description,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        Instant dateCreated
) {
}
