package starlight.backend.exception;

public class TalentNotFoundException extends RuntimeException{
    public TalentNotFoundException(long id) {
        super("Talent not found by id " + id);
    }
}
