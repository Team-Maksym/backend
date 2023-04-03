package starlight.backend.talent;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import starlight.backend.talent.model.request.TalentUpdateRequest;
import starlight.backend.talent.model.response.TalentFullInfo;
import starlight.backend.talent.model.response.TalentPagePagination;
import starlight.backend.talent.service.TalentServiceInterface;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/api/v1")
public class TalentController {
    private TalentServiceInterface talentService;

    @GetMapping("/talents")
    public TalentPagePagination pagination(@RequestParam(defaultValue = "0") @Min(0) int page,
                                           @RequestParam(defaultValue = "10") @Positive int size) {
        return talentService.talentPagination(page, size);
    }

    @PreAuthorize("hasRole('TALENT')")
    @GetMapping("/talents/{talent-id}")
    public Optional<TalentFullInfo> searchTalentById(@PathVariable("talent-id") long talentId) {
        return talentService.talentFullInfo(talentId);
    }

    @PreAuthorize("hasRole('TALENT')")
    @PatchMapping("/talents/{talent-id}")
    public TalentFullInfo updateTalentFullInfo(@PathVariable("talent-id") long talentId,
                                               @RequestBody TalentUpdateRequest talentUpdateRequest,
                                               Authentication auth) {
        if (auth != null && auth.isAuthenticated() &&
                (Objects.equals(auth.getName(), String.valueOf(talentId)))) {
            return talentService.updateTalentProfile(talentId, talentUpdateRequest);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you cannot change someone else's profile");
        }
    }

    @PreAuthorize("hasRole('TALENT')")
    @DeleteMapping( "/talents/{talent-id}")
    public void deleteTalent(@PathVariable("talent-id") long talentId,
                             Authentication auth) {
        if (auth != null && auth.isAuthenticated() &&
                (Objects.equals(auth.getName(), String.valueOf(talentId)))) {
            talentService.deleteTalentProfile(talentId);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "you cannot delete someone else's profile");
        }
    }
}