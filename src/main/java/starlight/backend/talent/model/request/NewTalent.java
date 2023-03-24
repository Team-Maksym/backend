package starlight.backend.talent.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record NewTalent(
        @NotBlank String full_name,
        @NotBlank String email,
        @NotBlank String password
)
 {
}
