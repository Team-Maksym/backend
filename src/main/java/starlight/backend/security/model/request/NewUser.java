package starlight.backend.security.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

@Builder
@Validated
public record NewUser(
        @NotBlank
        @Length(min = 3, max = 64)
        @Pattern(regexp = "^[a-zA-Z]{4,}(?: [a-zA-Z]+){0,2}$", message = "must not contain special characters")
        @JsonProperty("full_name")
        String fullName,

        @NotBlank
        @Email(regexp = "^[\\p{L}.'-]+@[\\p{L}.'-]+", message = "must be a valid email")
        String email,

        @NotBlank
        @Length(min = 8, max = 128)
        @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]+$",
        message = "must be between 8 and 128 characters, must contain at least one letter and one number")
        String password
) {

}
