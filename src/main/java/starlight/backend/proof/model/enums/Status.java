package starlight.backend.proof.model.enums;

public enum Status {
    DRAFT, PUBLISHED, HIDDEN;

    public String getStatus() {
        return name();
    }
}
