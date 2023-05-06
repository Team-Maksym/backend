package starlight.backend.exception;

public class SponsorAlreadyOnDeleteList extends RuntimeException {
    public SponsorAlreadyOnDeleteList(long sponsorId) {
        super("Sponsor with id " + sponsorId + " is already on delete list");
    }
}
