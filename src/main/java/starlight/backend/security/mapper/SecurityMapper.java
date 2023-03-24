package starlight.backend.security.mapper;

import org.mapstruct.Mapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import starlight.backend.talent.model.entity.UserEntity;
import starlight.backend.talent.model.response.CreatedTalent;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface SecurityMapper {

    default CreatedTalent toCreatedUser(UserEntity user){
        return CreatedTalent.builder()
                .name(user.getFullName())
                .email(user.getMail())
                .build();
    }

    default UserDetails toTalentDetails(UserEntity user) {
        return User.withUsername(user.getMail())
                .password(user.getPassword())
                .authorities(user.getAuthorities()
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList())
                .build();
    }
}