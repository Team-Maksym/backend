package starlight.backend.talent.model.response;

import lombok.Builder;

@Builder
public record CreatedTalent(
        String fullName,
        String email
) {
}
