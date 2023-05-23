package starlight.backend.proof;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import starlight.backend.proof.model.entity.ProofEntity;
import starlight.backend.proof.model.response.*;
import starlight.backend.skill.model.entity.SkillEntity;
import starlight.backend.skill.model.response.SkillWithCategory;
import starlight.backend.user.model.entity.UserEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface ProofMapper {
    default ProofInfo toProofInfo(ProofEntity proof) {
        return ProofInfo.builder()
                .id(proof.getProofId())
                .dateCreated(proof.getDateCreated())
                .description(proof.getDescription())
                .title(proof.getTitle())
                .status(proof.getStatus())
                .build();
    }

    default ProofInfoWithSkills toProofInfoWithSkills(ProofEntity proof) {
        return ProofInfoWithSkills.builder()
                .id(proof.getProofId())
                .dateCreated(proof.getDateCreated())
                .description(proof.getDescription())
                .title(proof.getTitle())
                .status(proof.getStatus())
                .skillWithCategoryList(proof.getSkills()
                        .stream()
                        .map(this::toSkillWithCategory)
                        .collect(Collectors.toCollection(LinkedList::new))
                )
                .build();
    }

    default ProofPagePagination toProofPagePagination(Page<ProofEntity> proofs) {
        return ProofPagePagination.builder()
                .total(proofs.getTotalElements())
                .data(proofs.getContent()
                        .stream()
                        .map(this::toProofInfo)
                        .toList())
                .build();
    }

    default ProofPagePaginationWithSkills toProofPagePaginationWithSkills(Page<ProofEntity> proofs) {
        return ProofPagePaginationWithSkills.builder()
                .total(proofs.getTotalElements())
                .data(proofs.getContent()
                        .stream()
                        .map(this::toProofInfoWithSkills)
                        .toList())
                .build();
    }

    default ProofFullInfo toProofFullInfo(ProofEntity proof) {
        return ProofFullInfo.builder()
                .title(proof.getTitle())
                .link(proof.getLink())
                .status(proof.getStatus())
                .dateCreated(proof.getDateCreated())
                .dateLastUpdated(proof.getDateLastUpdated())
                .description(proof.getDescription())
                .build();
    }

    default ProofFullInfoWithSkills toProofFullInfoWithSkills(ProofEntity proof) {
        return ProofFullInfoWithSkills.builder()
                .id(proof.getProofId())
                .title(proof.getTitle())
                .link(proof.getLink())
                .status(proof.getStatus())
                .dateCreated(proof.getDateCreated())
                .dateLastUpdated(proof.getDateLastUpdated())
                .description(proof.getDescription())
                .skillWithCategoryList(proof.getSkills()
                        .stream()
                        .map(this::toSkillWithCategory)
                        .collect(Collectors.toCollection(LinkedList::new))
                )
                .build();
    }


    default SkillWithCategory toSkillWithCategory(SkillEntity skillEntity) {
        return SkillWithCategory.builder()
                .skillId(skillEntity.getSkillId())
                .skill(skillEntity.getSkill())
                .category(skillEntity.getCategory())
                .build();
    }

    default ProofListWithSkills toProofListWithSkills(UserEntity user, List<ProofEntity> proofs){

        return ProofListWithSkills.builder()
                .data(proofs.stream()
                        .map(this::toProofFullInfoWithSkills)
                        .toList())
                .build();
    }
}
