package starlight.backend.talent.model.response;

import lombok.Builder;
import starlight.backend.talent.model.entity.PositionEntity;

import java.util.List;

@Builder
public record TalentFullInfo(
        String fullName,
        String email,
        Integer age,
        String avatar,
        String education,
        String experience,
        List<PositionEntity> positions
) {
}
