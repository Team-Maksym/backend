package starlight.backend.security.service;

import org.springframework.security.core.Authentication;
import starlight.backend.security.model.UserDetailsImpl;
import starlight.backend.security.model.request.NewUser;
import starlight.backend.security.model.response.SessionInfo;

import java.util.Objects;

public interface SecurityServiceInterface {
    SessionInfo register(NewUser newUser);

    SessionInfo loginInfo(Authentication auth);

    default boolean checkingLoggedAndToken(long talentId, Authentication auth) {
        return Objects.equals(auth.getName(), String.valueOf(talentId));
    }
    String getJWTToken(UserDetailsImpl authentication, long id);
    String createScope(UserDetailsImpl authentication);

    SessionInfo loginSponsor(Authentication auth);
    SessionInfo registerSponsor(NewUser newUser);
}
