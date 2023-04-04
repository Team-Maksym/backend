package starlight.backend.proof.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProofInfo(
        String title,
        String description,
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        LocalDateTime dateCreated
) {
}
