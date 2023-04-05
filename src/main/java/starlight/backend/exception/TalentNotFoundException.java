package starlight.backend.exception;

public class TalentNotFoundException extends RuntimeException{
    public TalentNotFoundException(Long id) {
        super("Talent not found by id " + id);
    }
}
