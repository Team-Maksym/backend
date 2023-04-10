package starlight.backend.security.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;
import starlight.backend.security.model.request.NewUser;
import starlight.backend.security.model.response.SessionInfo;

import java.util.Objects;

public interface SecurityServiceInterface {
    SessionInfo register(NewUser newUser);

    SessionInfo loginInfo(String userName);

    default boolean checkingLoggedAndTokenValid(long talentId, Authentication auth) {
        if (auth != null && auth.isAuthenticated() &&
                (Objects.equals(auth.getName(), String.valueOf(talentId)))) {
            return true;
        }
        return false;
    }
    default boolean checkingLogged(long talentId, Authentication auth) {
        if ((Objects.equals(auth.getName(), String.valueOf(talentId)))) {
            return true;
        }
        return false;
    }

}
