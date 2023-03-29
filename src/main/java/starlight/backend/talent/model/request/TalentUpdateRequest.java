package starlight.backend.talent.model.request;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record TalentUpdateRequest(
        String fullName,
        LocalDate birthday,
        String avatar,
        String education,
        String experience,
        List<String> positions
) {
}
