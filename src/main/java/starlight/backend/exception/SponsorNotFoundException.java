package starlight.backend.exception;

public class SponsorNotFoundException extends RuntimeException{
    public SponsorNotFoundException(long id) {
        super("Sponsor not found by id " + id);
    }
}
