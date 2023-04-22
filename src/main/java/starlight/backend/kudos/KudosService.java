package starlight.backend.kudos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import starlight.backend.exception.*;
import starlight.backend.kudos.model.entity.KudosEntity;
import starlight.backend.kudos.repository.KudosRepository;
import starlight.backend.proof.ProofRepository;
import starlight.backend.proof.model.entity.ProofEntity;
import starlight.backend.security.service.SecurityServiceInterface;
import starlight.backend.user.repository.UserRepository;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class KudosService {
    private SecurityServiceInterface securityService;
    private KudosRepository kudosRepository;
    private ProofRepository proofRepository;
    @PersistenceContext
    private EntityManager em;
    private UserRepository userRepository;


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
                        .getUserId()
                        .toString()
                        .equals(auth.getName()))
                .toList();
        return !kudosList.isEmpty();
    }

    public long getKudosOnProof(long proofId) {
        var proof = proofRepository.findById(proofId)
                .orElseThrow(() -> new ProofNotFoundException(proofId));
        //TODO: mb rewrite this
        log.info("Kudos on proof = {}", proof.getProofId());
        log.info("size = {}", proof.getKudos().size());

        return proof.getKudos().size();
    }


    public void addKudosOnProof(long proofId, Authentication auth) {
        if (isItMyProof(proofId, auth)) {
            throw new UserCannotAddKudosToTheirAccount();
        }
        if (isProofAlreadyHaveKudosFromUser(proofId, auth)){
            throw new ProofAlreadyHaveKudosFromUser();
        }

        var proof = proofRepository.findById(proofId)
                .orElseThrow(() -> new ProofNotFoundException(proofId));
        var follower= userRepository.findById(Long.valueOf(auth.getName()))
                .orElseThrow(() -> new UserNotFoundException(auth.getName()));
        var owner = userRepository.findById(proof.getUser().getUserId())
                .orElseThrow(() -> new UserNotFoundException(proof.getUser().getUserId()));

        var kudos = KudosEntity.builder()
                .followerId(follower.getUserId())
                .createData(Instant.now())
                .proof(proof)
                .owner(owner)
                .build();

        kudosRepository.save(kudos);
    }
}