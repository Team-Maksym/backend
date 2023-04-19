package starlight.backend.kudos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import starlight.backend.kudos.model.entity.KudosEntity;
import starlight.backend.proof.model.entity.ProofEntity;
import starlight.backend.security.service.SecurityServiceInterface;

@Service
@AllArgsConstructor
public class KudosService {
    private SecurityServiceInterface securityService;
    @PersistenceContext
    private EntityManager em;

    private boolean isItMyProof(long proofId, Authentication auth) {
        var talentId = em.find(ProofEntity.class, proofId).getUser().getUserId();
        return securityService.checkingLoggedAndToken(talentId, auth);
    }
    private boolean isProofAlreadyHaveKudosFromUser(long kudosId, Authentication auth) {
        var ownerId = em.find(KudosEntity.class, kudosId).getOwner().getUserId();
        return securityService.checkingLoggedAndToken(ownerId, auth);
    }

}