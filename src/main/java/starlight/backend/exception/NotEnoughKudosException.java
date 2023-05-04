package starlight.backend.exception;

public class NotEnoughKudosException extends RuntimeException{
    public NotEnoughKudosException() {
        super("Not enough kudos on your account");
    }
}
