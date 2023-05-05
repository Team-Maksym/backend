package starlight.backend.exception;

public class ProofNotFoundException extends RuntimeException{
    public ProofNotFoundException(long id) {
        super("Proof not found by id " + id);
    }
    public ProofNotFoundException(String id) {
        super("Proof not found by id " + id);
    }
}
