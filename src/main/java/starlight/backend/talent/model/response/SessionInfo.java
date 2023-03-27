package starlight.backend.talent.model.response;

import lombok.Builder;

@Builder
public record SessionInfo(
        String token,
        Long user_id
) {
}
