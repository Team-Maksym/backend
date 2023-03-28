package starlight.backend.security.service;

import org.springframework.security.core.Authentication;
import starlight.backend.talent.model.request.NewUser;
import starlight.backend.talent.model.response.SessionInfo;

public interface UserServiceInterface {
    SessionInfo register(NewUser newUser);

    SessionInfo loginInfo(Authentication authentication);
}
