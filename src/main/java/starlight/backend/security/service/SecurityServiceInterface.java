package starlight.backend.security.service;

import org.springframework.security.core.Authentication;
import starlight.backend.security.model.request.NewUser;
import starlight.backend.security.model.response.SessionInfo;

import java.util.Objects;

public interface SecurityServiceInterface {
    SessionInfo register(NewUser newUser);

    SessionInfo loginInfo(String userName);

    default boolean checkingLoggedAndToken(long talentId, Authentication auth) {
        return auth == null || !auth.isAuthenticated() ||
                (!Objects.equals(auth.getName(), String.valueOf(talentId)));
    }

    default boolean checkingLogged(long talentId, Authentication auth) {
        return Objects.equals(auth.getName(), String.valueOf(talentId));
    }
}
