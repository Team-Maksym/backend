package starlight.backend.proof.model.enums;

public enum Status {
    DRAFT, PUBLISHER, HIDDEN;

    public String getStatus() {
        return name();
    }
}
