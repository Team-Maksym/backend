package starlight.backend.talent.model.response;

import lombok.Builder;

@Builder
public record TalentProfile(
        String fullName,
        String position,
        String avatar
) {
}
