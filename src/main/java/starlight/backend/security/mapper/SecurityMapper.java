package starlight.backend.security.mapper;

import org.mapstruct.Mapper;
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
}