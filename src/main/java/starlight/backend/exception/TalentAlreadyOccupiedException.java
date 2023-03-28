package starlight.backend.exception;

public class TalentAlreadyOccupiedException extends RuntimeException{
    public TalentAlreadyOccupiedException(String email) {
        super("Email '" + email + "' is already occupied");
    }
}
