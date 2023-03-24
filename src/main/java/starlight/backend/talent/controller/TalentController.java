package starlight.backend.talent.controller;

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
public class TalentController {
    private TalentServiceInterface talentService;

    @GetMapping("/talents")
    public TalentPagePagination pagination (@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        return talentService.talentPagination(page, size);
    }
//TODO pagination default
    @GetMapping("/talents/{talentId}")
    public Optional<TalentFullInfo> searchTalentById(@PathVariable(value = "talentId") Long talentId) {
        return talentService.talentFullInfo(talentId);
    }
}
