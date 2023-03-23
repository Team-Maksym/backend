package starlight.backend.talent.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record NewTalent(
        @NotBlank String name,
        @NotBlank String email,
        @NotBlank String password
)
 {
}
