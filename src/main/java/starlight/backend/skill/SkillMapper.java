package starlight.backend.skill;

import org.mapstruct.Mapper;
import starlight.backend.proof.model.entity.ProofEntity;
import starlight.backend.proof.model.response.ProofWithSkills;
import starlight.backend.skill.model.entity.SkillEntity;
import starlight.backend.skill.model.response.SkillList;
import starlight.backend.skill.model.response.SkillListWithPagination;
import starlight.backend.skill.model.response.SkillWithCategory;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface SkillMapper {
    default SkillListWithPagination toSkillListWithPagination(List<SkillEntity> skills, long pageNumber) {
        return SkillListWithPagination.builder()
                .data(skills.stream()
                        .map(this::toSkillWithCategory)
                        .toList())
                .total(pageNumber)
                .build();
    }

    default SkillList toSkillList(List<SkillEntity> skills) {
        return SkillList.builder()
                .skills(skills.stream()
                        .map(this::toSkillWithCategory)
                        .toList())
                .build();
    }

    default SkillWithCategory toSkillWithCategory(SkillEntity skill) {
        return SkillWithCategory.builder()
                .skillId(skill.getSkillId())
                .category(skill.getCategory())
                .skill(skill.getSkill())
                .build();
    }

    default ProofWithSkills toProofWithSkills(ProofEntity proofEntity) {
        return ProofWithSkills.builder()
                .title(proofEntity.getTitle())
                .link(proofEntity.getLink())
                .status(proofEntity.getStatus())
                .description(proofEntity.getDescription())
                .dateLastUpdated(proofEntity.getDateLastUpdated())
                .dateCreated(proofEntity.getDateCreated())
                .skill(proofEntity.getSkills()
                        .stream()
                        .map(this::toSkillWithCategory)
                        .toList())
                .build();
    }

}
