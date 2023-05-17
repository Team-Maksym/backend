package starlight.backend.proof.model.response;

import lombok.Builder;

@Builder
public record SkillWithCategory(
       String skill,
       String category
) {}
