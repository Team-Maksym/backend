package starlight.backend.talent;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import starlight.backend.talent.model.entity.PositionEntity;
import starlight.backend.talent.model.entity.TalentEntity;
import starlight.backend.talent.model.response.TalentFullInfo;
import starlight.backend.talent.model.response.TalentPagePagination;
import starlight.backend.talent.model.response.TalentProfile;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface TalentMapper {
    default TalentProfile toTalentProfile(TalentEntity talent) {
        return TalentProfile.builder()
                .fullName(talent.getFullName())
                .email(talent.getMail())
                .position(talent.getPositions().stream().map(PositionEntity::getPosition).toList())
                .avatarUrl(talent.getAvatarUrl())
                .build();
    }

    default TalentPagePagination toTalentPagePagination(Page<TalentEntity> talentEntities) {
        return TalentPagePagination.builder()
                .talentProfileList(talentEntities.getContent().
                        stream().map(this::toTalentProfile).toList())
                .totalTalents(talentEntities.getTotalElements())
                .build();
    }

    default TalentFullInfo toTalentFullInfo(TalentEntity talent) {
        return TalentFullInfo.builder()
                .fullName(talent.getFullName())
                .mail(talent.getMail())
                .age(talent.getAge())
                .avatar(talent.getAvatarUrl())
                .experience(talent.getExperience())
                .education(talent.getEducation())
                .positions(talent.getPositions().stream().toList())
                .build();
    }
}

