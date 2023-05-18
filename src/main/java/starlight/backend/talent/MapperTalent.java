package starlight.backend.talent;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import starlight.backend.proof.model.entity.ProofEntity;
import starlight.backend.proof.model.response.ProofInfoWithSkills;
import starlight.backend.skill.SkillMapper;
import starlight.backend.talent.model.response.TalentWithSkills;
import starlight.backend.user.model.entity.PositionEntity;
import starlight.backend.user.model.entity.UserEntity;
import starlight.backend.talent.model.response.TalentFullInfo;
import starlight.backend.talent.model.response.TalentPagePagination;
import starlight.backend.talent.model.response.TalentProfile;

import java.util.LinkedList;
import java.util.stream.Collectors;

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
}

