package starlight.backend.user.model.enums;

public enum Status {
    DRAFT, PUBLISHER, HIDDEN;

    public String getStatus() {
        return name();
    }
}
