package starlight.backend.kudos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import starlight.backend.exception.*;
import starlight.backend.kudos.model.entity.KudosEntity;
import starlight.backend.kudos.model.response.KudosOnProof;
import starlight.backend.kudos.repository.KudosRepository;
import starlight.backend.proof.ProofRepository;
import starlight.backend.proof.model.entity.ProofEntity;
import starlight.backend.security.model.enums.Role;
import starlight.backend.security.service.SecurityServiceInterface;
import starlight.backend.sponsor.SponsorRepository;
import starlight.backend.user.repository.UserRepository;

import java.time.Instant;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class KudosService {
    private SecurityServiceInterface securityService;
    private KudosRepository kudosRepository;
    private ProofRepository proofRepository;
    private UserRepository userRepository;
    private SponsorRepository sponsorRepository;
    @PersistenceContext
    private EntityManager em;

    private boolean isItMyProof(long proofId, Authentication auth) {
        var talentId = em.find(ProofEntity.class, proofId).getUser().getUserId();
        return securityService.checkingLoggedAndToken(talentId, auth);
    }

    private boolean isProofAlreadyHaveKudosFromUser(long proofId, Authentication auth) {
        var proof = proofRepository.findById(proofId)
                .orElseThrow(() -> new ProofNotFoundException(proofId));

        var kudosList = proof.getKudos()
                .stream()
                .filter(k -> k.getOwner()
                        .getSponsorId()
                        .toString()
                        .equals(auth.getName()))
                .toList();
        return !kudosList.isEmpty();
    }

    @Transactional(readOnly = true)
    public KudosOnProof getKudosOnProof(long proofId, Authentication auth) {
        var proof = proofRepository.findById(proofId)
                .orElseThrow(() -> new ProofNotFoundException(proofId));
        var kudos = proof.getKudos();
        int countKudos = kudos
                .stream()
                .mapToInt(KudosEntity::getCountKudos)
                .sum();
        log.info("countKudos = {}", countKudos);
        if (auth != null){
            for (GrantedAuthority grantedAuthority : auth.getAuthorities()) {
                if (grantedAuthority.getAuthority().equals(Role.SPONSOR.getAuthority())) {
                    log.info("Is Sponsor = {}", grantedAuthority.getAuthority().equals(Role.SPONSOR.getAuthority()));
                    return new KudosOnProof(countKudos, isProofAlreadyHaveKudosFromUser(proofId, auth));
                }
            }
        }
        return new KudosOnProof(countKudos, false);
    }


    public void addKudosOnProof(long proofId, int kudos, Authentication auth) {
        if (auth == null) {
            throw new AuthorizationFailureException();
        }
        for (GrantedAuthority grantedAuthority : auth.getAuthorities()) {
            if (!grantedAuthority.getAuthority().equals(Role.SPONSOR.getAuthority())) {
                throw new TalentCanNotAddKudos();
            }
        }
        if (isItMyProof(proofId, auth)) {
            throw new UserCannotAddKudosToTheirAccount();
        }
        var proof = proofRepository.findById(proofId)
                .orElseThrow(() -> new ProofNotFoundException(proofId));
        var owner = sponsorRepository.findById(Long.valueOf(auth.getName()))
                .orElseThrow(() -> new UserNotFoundException(auth.getName()));
        if (kudos > owner.getUnusedKudos()) {
            throw new NotEnoughKudosException();
        }
        var follower = userRepository.findById(proof.getUser().getUserId())
                .orElseThrow(() -> new UserNotFoundException(auth.getName()));
        var kudosBuild = KudosEntity.builder()
                .followerId(follower.getUserId())
                .createData(Instant.now())
                .proof(proof)
                .owner(owner)
                .countKudos(kudos)
                .build();
        kudosRepository.save(kudosBuild);
        sponsorRepository.findById(owner.getSponsorId()).map(sponsor -> {
            sponsor.setUnusedKudos(owner.getUnusedKudos() - kudos);
            sponsorRepository.save(sponsor);
            return null;
        });
    }
}
