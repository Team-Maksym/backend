package starlight.backend.sponsor.model.request;

import lombok.Builder;

@Builder
public record SponsorUpdateRequest(
        String fullName,
        String avatar,
        String company,
        int unusedKudos
) {
}
