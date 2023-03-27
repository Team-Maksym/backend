package starlight.backend.security.service;

import starlight.backend.security.model.UserDetailsImpl;
import starlight.backend.talent.model.entity.UserEntity;
import starlight.backend.talent.model.request.NewUser;

public interface UserServiceInterface {
    UserEntity register(NewUser newUser);
    String getJWTToken(UserDetailsImpl authentication);
}
