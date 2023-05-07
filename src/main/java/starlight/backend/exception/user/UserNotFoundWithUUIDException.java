package starlight.backend.exception.user;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class UserNotFoundWithUUIDException extends RuntimeException{
    public UserNotFoundWithUUIDException(UUID uuid) {
        super("User with UUID = " + uuid + "not found");
        log.debug("User with UUID = " + uuid + "not found in DeleyedDeleteRepository");
    }

    public UserNotFoundWithUUIDException(String uuid) {
        super("User with UUID = " + uuid + "not found");
        log.debug("User with UUID = " + uuid + "not found in DeleyedDeleteRepository");
    }
}