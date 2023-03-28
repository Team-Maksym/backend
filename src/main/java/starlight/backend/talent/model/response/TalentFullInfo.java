package starlight.backend.talent.model.response;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record TalentFullInfo(
        String fullName,
        String email,
        LocalDate birthday,
        String avatar,
        String education,
        String experience,
        List<String> positions
) {
}
