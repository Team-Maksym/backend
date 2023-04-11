package starlight.backend.proof.model.request;

import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Builder
public record ProofUpdateRequest(
        @Length(max = 255)
        @NotBlank
        String title,

        @Length(max = 1000)
        @NotBlank
        String description,

        @URL
        String link

) {
}

