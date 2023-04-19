package starlight.backend.exception;

public class UserCannotAddKudosToTheirAccount extends RuntimeException {
    public UserCannotAddKudosToTheirAccount() {
        super("User cannot add kudos to their account");
    }
}
