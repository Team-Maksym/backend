package starlight.backend.talent;

import starlight.backend.talent.model.entity.PositionEntity;
import starlight.backend.talent.model.entity.TalentEntity;
import starlight.backend.talent.model.response.CreatedTalent;
import starlight.backend.talent.model.response.TalentPagePagination;
import starlight.backend.talent.model.response.TalentProfile;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface TalentMapper {
    CreatedTalent toCreatedTalent(TalentEntity talentEntity);

    default TalentProfile toTalentProfile(TalentEntity talent) {
        return TalentProfile.builder()
                .fullName(talent.getFullName())
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
}

