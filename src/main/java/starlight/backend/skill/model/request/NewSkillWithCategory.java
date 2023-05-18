package starlight.backend.skill.model.request;

import lombok.Builder;

@Builder
public record NewSkillWithCategory(
        String category,
        String skill
) {
}
