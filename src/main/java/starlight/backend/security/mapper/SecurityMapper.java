package starlight.backend.security.mapper;

import org.mapstruct.Mapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import starlight.backend.talent.model.entity.TalentEntity;
import starlight.backend.talent.model.response.CreatedTalent;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface SecurityMapper {

    default CreatedTalent toCreatedUser(TalentEntity talentEntity){
        return CreatedTalent.builder()
                .name(talentEntity.getFullName())
                .email(talentEntity.getMail())
                .build();
    }

    default UserDetails toTalentDetails(TalentEntity talentEntity) {
        return User.withUsername(talentEntity.getMail())
                .password(talentEntity.getPassword())
                .authorities(talentEntity.getAuthorities()
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList())
                .build();
    }
}