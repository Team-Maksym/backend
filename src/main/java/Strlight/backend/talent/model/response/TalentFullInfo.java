package Strlight.backend.talent.model.response;

import Strlight.backend.talent.model.entity.PositionEntity;
import lombok.Builder;

import java.util.List;

@Builder
public record TalentFullInfo(
        String fullName,
        String mail,
        Integer age,
        String avatar,
        String education,
        String experience,
        List<PositionEntity> positions
) {
}
