package starlight.backend.talent.model.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record TalentProfile (
            @NotNull
            String fullName,
            //@NotNull
            //String email,
            @NotNull
            String position,
            String avatar
){}
