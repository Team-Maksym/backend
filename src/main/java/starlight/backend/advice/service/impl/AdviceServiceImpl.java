package starlight.backend.advice.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import starlight.backend.advice.repository.DelayedDeleteRepository;
import starlight.backend.sponsor.SponsorRepository;

import java.time.Instant;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class AdviceServiceImpl {
    private DelayedDeleteRepository delayedDeleteRepository;
    private SponsorRepository sponsorRepository;

    @Scheduled(cron = "**0 0 * * ***") // 24 hours
    public void deleteAccounts() {
        var accounts = delayedDeleteRepository.findAll();

        for (var account : accounts) {
            if (account.getDeleteDate().isAfter(Instant.now())) {
                continue;
            }
            var userId = account.getEntityID();
            var exists = sponsorRepository.existsBySponsorId(userId);
            if (!exists) {
                log.info("User not found in DelayedDeleteRepository: {}", userId);
                continue;
            }
            if (exists) {
                log.info("Deleting user: {}", userId);
                var sponsor = sponsorRepository.findById(userId).get();
                sponsor.getAuthorities().clear();
                if (!sponsor.getKudos().isEmpty()) {
                    for (var kudos : sponsor.getKudos()) {
                        kudos.setOwner(null);
                    }

                    sponsorRepository.deleteById(userId);
                }
                delayedDeleteRepository.deleteById(userId);
            } else {
                log.info("User not found in SponsorRepository: {}", userId);


            }
        }
    }
}