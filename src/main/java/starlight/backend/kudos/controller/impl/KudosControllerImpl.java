package starlight.backend.kudos.controller.impl;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import starlight.backend.kudos.KudosService;

@AllArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
@Validated
@Tag(name = "Kudos", description = "Kudos API")
@RestController
public class KudosControllerImpl  {
    KudosService kudosService;

    @GetMapping("/proofs/{proof-id}/kudos")
    public long getKudosOnProof(@PathVariable("proof-id") long proofId) {
        log.info("@GetMapping(\"/proofs/{proof-id}/kudos\")");

        log.info("Getting proof-id = {}", proofId);
        return kudosService.getKudosOnProof(proofId);
    }

    @PreAuthorize("hasRole('TALENT')")
    @PostMapping("/proofs/{proof-id}/kudos")
    @ResponseStatus(HttpStatus.CREATED)
    public void kudosTwo(@PathVariable("proof-id") long proofId, Authentication auth) {
        log.info("@PostMapping(\"/proofs/{proof-id}/kudos\")");

        log.info("Getting proof-id = {}", proofId);
        kudosService.addKudosOnProof(proofId, auth);

    }
}
