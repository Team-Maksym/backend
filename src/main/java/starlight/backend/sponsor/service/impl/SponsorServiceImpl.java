package starlight.backend.sponsor.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import starlight.backend.advice.config.AdviceConfiguration;
import starlight.backend.advice.model.entity.DelayedDeleteEntity;
import starlight.backend.advice.model.enums.DeletingEntityType;
import starlight.backend.advice.repository.DelayedDeleteRepository;
import starlight.backend.advice.service.impl.AdviceServiceImpl;
import starlight.backend.exception.SponsorNotFoundException;
import starlight.backend.security.service.SecurityServiceInterface;
import starlight.backend.sponsor.SponsorRepository;
import starlight.backend.sponsor.model.entity.SponsorEntity;
import starlight.backend.sponsor.model.enums.SponsorStatus;
import starlight.backend.sponsor.model.response.UnusableKudos;
import starlight.backend.sponsor.service.SponsorServiceInterface;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@AllArgsConstructor
@Service
@Transactional
public class SponsorServiceImpl implements SponsorServiceInterface {
    private SponsorRepository sponsorRepository;
    private SecurityServiceInterface securityService;
    private DelayedDeleteRepository delayedDeleteRepository;
    private AdviceServiceImpl adviceService;
    private AdviceConfiguration adviceConfiguration;
    @PersistenceContext
    private EntityManager em;
    @Override
    public UnusableKudos getUnusableKudos(long sponsorId) {
        if (!sponsorRepository.existsBySponsorId(sponsorId)) {
            throw new SponsorNotFoundException(sponsorId);
        }
        return new UnusableKudos(em.find(SponsorEntity.class, sponsorId).getUnusedKudos());
    }

    @Override
    public void deleteSponsor(long sponsorId, Authentication auth) {
        if (!sponsorRepository.existsBySponsorId(sponsorId)) {
            throw new SponsorNotFoundException(sponsorId);
        }
        if (!securityService.checkingLoggedAndToken(sponsorId, auth)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You cannot delete this sponsor");
        }

        sponsorRepository.findById(sponsorId).ifPresent(sponsor -> {
            delayedDeleteRepository.save(DelayedDeleteEntity.builder()
                            .entityID(sponsorId)
                            .deletingEntityType(DeletingEntityType.SPONSOR)
                            .deleteDate(Instant.now().plus(adviceConfiguration.delayDays(), ChronoUnit.DAYS))
                            .userDeletingProcessUUID(UUID.randomUUID())
                            .build()

            );

            sponsor.setStatus(SponsorStatus.DELETING);
            sponsorRepository.save(sponsor);
        });
    }
}
