package starlight.backend.talent;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import starlight.backend.talent.model.response.TalentFullInfo;
import starlight.backend.talent.model.response.TalentPagePagination;
import starlight.backend.talent.service.TalentServiceInterface;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    // @PreAuthorize("#talentId == authentication.name")//????
    @PreAuthorize("hasRole('TALENT')")
    @PatchMapping("/talents/{talent-id}")
    public TalentFullInfo updateTalentFullInfo(@PathVariable("talent-id") long talentId,
                                               @RequestBody TalentUpdateRequest talentUpdateRequest,
                                               Authentication auth) {
        log.info("auth.name={}", auth.getName());
        if (auth != null && auth.isAuthenticated() &&
                (Objects.equals(auth.getName(), String.valueOf(talentId)))) {
            return talentService.updateTalentProfile(talentId, talentUpdateRequest);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you cannot change someone else's profile");
        }
    }
/*
    @PreAuthorize("hasRole('TALENT')")
    @DeleteMapping(path = "/talents/{talent-id}",
            produces = {MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE})
    public void deleteTalent(@PathVariable("talent-id") long talentId,
                             Authentication auth) {
        if (auth != null && auth.isAuthenticated() &&
                (Objects.equals(auth.getName(), String.valueOf(talentId)))) {
            talentService.deleteTalentProfile(talentId);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "you cannot delete someone else's profile");
        }
    }
 */
}
