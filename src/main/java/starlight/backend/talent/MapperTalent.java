package starlight.backend.talent;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import starlight.backend.skill.model.entity.SkillEntity;
import starlight.backend.skill.model.response.SkillWithCategory;
import starlight.backend.talent.model.response.*;
import starlight.backend.user.model.entity.PositionEntity;
import starlight.backend.user.model.entity.UserEntity;
import starlight.backend.talent.model.response.TalentFullInfo;
import starlight.backend.talent.model.response.TalentPagePagination;
import starlight.backend.talent.model.response.TalentProfile;
import starlight.backend.user.model.entity.PositionEntity;
import starlight.backend.user.model.entity.UserEntity;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface MapperTalent {
    default TalentProfile toTalentProfile(UserEntity user) {
        return TalentProfile.builder()
                .fullName(user.getFullName())
                .id(user.getUserId())
                .position(user.getPositions().stream()
                        .findAny()
                        .map(PositionEntity::getPosition)
                        .orElse(null))
                .avatar(user.getAvatar())
                .build();
    }

    default TalentPagePagination toTalentPagePagination(Page<UserEntity> user) {
        return TalentPagePagination.builder()
                .data(user.getContent().
                        stream().map(this::toTalentProfile).toList())
                .total(user.getTotalElements())
                .build();
    }

    default TalentFullInfo toTalentFullInfo(UserEntity user) {
        return TalentFullInfo.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .birthday(user.getBirthday())
                .avatar(user.getAvatar())
                .experience(user.getExperience())
                .education(user.getEducation())
                .positions(user.getPositions().stream().map(PositionEntity::getPosition).toList())
                .build();
    }

    default TalentPagePaginationWithFilterSkills toTalentListWithPaginationAndFilter(Page<UserEntity> sortedTalent){
        return TalentPagePaginationWithFilterSkills.builder()
                .data(sortedTalent.getContent().stream()
                        .map(this::toTalentWithSkills)
                        .toList())
                .total(sortedTalent.getTotalElements())
                .build();
    }

    default TalentWithSkills toTalentWithSkills(UserEntity user) {
        return TalentWithSkills.builder()
                .id(user.getUserId())
                .fullName(user.getFullName())
                .avatar(user.getAvatar())
                .position(user.getPositions().stream()
                        .findAny()
                        .map(PositionEntity::getPosition)
                        .orElse(null))
                .skill(user.getTalentSkills()
                        .stream()
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
}

