package starlight.backend.security.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record NewUser(
        @NotBlank
        String full_name,
        @NotBlank
        String email,
        @NotBlank
        String password
) {}
