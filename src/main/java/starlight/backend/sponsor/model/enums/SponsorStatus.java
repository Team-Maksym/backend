package starlight.backend.sponsor.model.enums;

public enum SponsorStatus {
    ACTIVE,DELETING,DELETED;

    public static SponsorStatus fromString(String status){
        return valueOf(status.toUpperCase());
    }
}
