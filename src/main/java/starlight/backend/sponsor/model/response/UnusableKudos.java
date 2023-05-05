package starlight.backend.sponsor.model.response;

import lombok.Builder;

@Builder
public record UnusableKudos(
        int kudosCount
) {}
