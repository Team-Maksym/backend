package starlight.backend.sponsor.model.response;

import lombok.Builder;

import java.util.List;

@Builder
public record UnusableKudos(
        int total,
        List<KudosWithProofId> data
) {}
