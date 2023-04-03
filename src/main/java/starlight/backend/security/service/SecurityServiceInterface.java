package starlight.backend.security.service;

import starlight.backend.security.model.request.NewUser;
import starlight.backend.security.model.response.SessionInfo;

public interface SecurityServiceInterface {
    SessionInfo register(NewUser newUser);

    SessionInfo loginInfo(String userName);
}
