package starlight.backend.exception;

public class UserCanNotEditProofNotInDraftException extends RuntimeException {
    public UserCanNotEditProofNotInDraftException() {
        super("You cannot change proof with status not a DRAFT.");
    }
}
