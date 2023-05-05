package starlight.backend.exception;

public class ProofAlreadyHaveKudosFromUser extends RuntimeException {
    public ProofAlreadyHaveKudosFromUser() {
        super("Proof already have kudos from user");
    }
}
