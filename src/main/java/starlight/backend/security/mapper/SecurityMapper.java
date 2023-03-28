package starlight.backend.security.mapper;

import org.mapstruct.Mapper;
import starlight.backend.security.model.UserDetailsImpl;
import starlight.backend.talent.model.entity.UserEntity;
import starlight.backend.talent.model.response.SessionInfo;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface SecurityMapper {
    default SessionInfo toSessionInfo(UserEntity user, String token) {
        return SessionInfo.builder()
                .token(token)
                .user_id(user.getUserId())
                .build();
    }
    default UserDetailsImpl toUserDetailsImpl(UserEntity user) {
        return new UserDetailsImpl(
                user.getEmail(),
                user.getPassword());
    }
}