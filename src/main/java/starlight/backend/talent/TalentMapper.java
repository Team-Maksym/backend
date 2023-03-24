package starlight.backend.talent;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import starlight.backend.talent.model.entity.PositionEntity;
import starlight.backend.talent.model.entity.UserEntity;
import starlight.backend.talent.model.response.Position;
import starlight.backend.talent.model.response.TalentFullInfo;
import starlight.backend.talent.model.response.TalentPagePagination;
import starlight.backend.talent.model.response.TalentProfile;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface TalentMapper {
    default TalentProfile toTalentProfile(UserEntity user) {
        return TalentProfile.builder()
                .fullName(user.getFullName())
                .position(user.getPositions().stream().map(PositionEntity::getPosition).toList().get(0))
                .avatar(user.getAvatar())
                .build();
    }

    default TalentPagePagination toTalentPagePagination(Page<UserEntity> userEntities) {
        return TalentPagePagination.builder()
                .data(userEntities.getContent().
                        stream().map(this::toTalentProfile).toList())
                .totalTalents(userEntities.getTotalElements())
                .build();
    }

    default TalentFullInfo toTalentFullInfo(UserEntity user) {
        return TalentFullInfo.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .age(user.getAge())
                .avatar(user.getAvatar())
                .experience(user.getExperience())
                .education(user.getEducation())
                .positions(user.getPositions().stream().map(PositionEntity::getPosition).toList())
                .build();
    }
}

