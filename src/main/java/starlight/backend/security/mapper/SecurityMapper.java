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
                .fullName(user.getFullName())
                .email(user.getEmail())
                .build();
    }
}