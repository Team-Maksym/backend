package starlight.backend.skill.model.request;

import lombok.Builder;

import java.util.List;

@Builder
public record DeleteIdSkills(
        List<Long> skillsId
) {
}
