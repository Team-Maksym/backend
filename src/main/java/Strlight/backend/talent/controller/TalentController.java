package Strlight.backend.talent.controller;

import Strlight.backend.talent.model.response.TalentPagePagination;
import Strlight.backend.talent.service.TalentServiceInterface;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TalentController {
    TalentServiceInterface talentService;

    @GetMapping("/talents")
    TalentPagePagination pagination (@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        return talentService.talentPagination(page, size);
    }
}
