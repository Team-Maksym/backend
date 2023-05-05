package starlight.backend.sponsor.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import starlight.backend.exception.ProofNotFoundException;
import starlight.backend.exception.SponsorNotFoundException;
import starlight.backend.sponsor.SponsorRepository;
import starlight.backend.sponsor.model.entity.SponsorEntity;
import starlight.backend.sponsor.model.response.UnusableKudos;
import starlight.backend.sponsor.service.SponsorServiceInterface;

@AllArgsConstructor
@Service
@Transactional
public class SponsorServiceImpl implements SponsorServiceInterface {
    private SponsorRepository sponsorRepository;
    @PersistenceContext
    private EntityManager em;
    @Override
    public UnusableKudos getUnusableKudos(long sponsorId) {
        if (!sponsorRepository.existsBySponsorId(sponsorId)) {
            throw new SponsorNotFoundException(sponsorId);
        }
        return new UnusableKudos(em.find(SponsorEntity.class, sponsorId).getUnusedKudos());
    }
}
