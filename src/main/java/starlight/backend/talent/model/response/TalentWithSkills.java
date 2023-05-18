package starlight.backend.talent.model.response;

import lombok.Builder;
import starlight.backend.skill.model.entity.SkillEntity;
import starlight.backend.skill.model.response.SkillWithCategory;

import java.util.List;

@Builder
public record TalentWithSkills(
        long id,
        String fullName,
        String position,
        List<SkillWithCategory> skill
) {}