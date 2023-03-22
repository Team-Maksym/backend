package Strlight.backend.talent;

import Strlight.backend.talent.model.entity.PositionEntity;
import Strlight.backend.talent.model.entity.TalentEntity;
import Strlight.backend.talent.model.response.TalentPagePagination;
import Strlight.backend.talent.model.response.TalentProfile;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface TalentMapper {

        default TalentProfile toTalentProfile(TalentEntity talent){
            return  TalentProfile.builder()
                    .fullName(talent.getFullName())
                    .position(talent.getPositions().stream().map(PositionEntity::getPosition).toList())
                    .avatarUrl(talent.getAvatarUrl())
                    .build();
        }

        default TalentPagePagination toTalentPagePagination(Page<TalentEntity> talentEntities)
        {
            return TalentPagePagination.builder()
                    .talentProfileList(talentEntities.getContent().
                            stream().map(this::toTalentProfile).toList())
                    .totalTalents(talentEntities.getTotalElements())
                    .totalPage(talentEntities.getTotalPages())
                    .build();
        }
}
