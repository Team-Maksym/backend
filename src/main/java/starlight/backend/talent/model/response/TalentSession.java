package starlight.backend.talent.model.response;

import lombok.Builder;

@Builder
public record TalentSession(
        long id,
        TalentProfile talentProfile
) {
}
