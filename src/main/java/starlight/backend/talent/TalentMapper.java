package starlight.backend.talent;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import starlight.backend.talent.model.entity.PositionEntity;
import starlight.backend.talent.model.entity.UserEntity;
import starlight.backend.talent.model.response.TalentFullInfo;
import starlight.backend.talent.model.response.TalentPagePagination;
import starlight.backend.talent.model.response.TalentProfile;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface TalentMapper {
    default TalentProfile toTalentProfile(UserEntity user) {
        return TalentProfile.builder()
                .fullName(user.getFullName())
                .email(user.getMail())
                .position(user.getPositions().stream().map(PositionEntity::getPosition).toList())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }

    default TalentPagePagination toTalentPagePagination(Page<UserEntity> userEntities) {
        return TalentPagePagination.builder()
                .talentProfileList(userEntities.getContent().
                        stream().map(this::toTalentProfile).toList())
                .totalTalents(userEntities.getTotalElements())
                .build();
    }

    default TalentFullInfo toTalentFullInfo(UserEntity user) {
        return TalentFullInfo.builder()
                .fullName(user.getFullName())
                .mail(user.getMail())
                .age(user.getAge())
                .avatar(user.getAvatarUrl())
                .experience(user.getExperience())
                .education(user.getEducation())
                .positions(user.getPositions().stream().toList())
                .build();
    }
}
