package Strlight.backend.talent.model.response;

import Strlight.backend.talent.model.entity.PositionEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;
import java.util.Set;

@Builder
public record TalentProfile (
            @NotNull
            String fullName,
            @NotNull
            List<String> position,
            String avatarUrl
    ){}
