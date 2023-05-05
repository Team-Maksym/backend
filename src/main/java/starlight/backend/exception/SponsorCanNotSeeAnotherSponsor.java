package starlight.backend.exception;

public class SponsorCanNotSeeAnotherSponsor extends RuntimeException{
    public SponsorCanNotSeeAnotherSponsor() {
        super("Sponsor can't see another sponsor");
    }
}
