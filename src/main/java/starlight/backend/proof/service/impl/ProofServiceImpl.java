package starlight.backend.proof.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import starlight.backend.exception.PageNotFoundException;
import starlight.backend.exception.ProofNotFoundException;
import starlight.backend.exception.TalentNotFoundException;
import starlight.backend.proof.ProofMapper;
import starlight.backend.proof.ProofRepository;
import starlight.backend.proof.model.entity.ProofEntity;
import starlight.backend.proof.model.enums.Status;
import starlight.backend.proof.model.request.ProofAddRequest;
import starlight.backend.proof.model.response.ProofFullInfo;
import starlight.backend.proof.model.response.ProofPagePagination;
import starlight.backend.proof.service.ProofServiceInterface;
import starlight.backend.security.service.SecurityServiceInterface;
import starlight.backend.user.model.entity.UserEntity;
import starlight.backend.user.repository.UserRepository;

import java.net.URI;
import java.time.Instant;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ProofServiceImpl implements ProofServiceInterface {
    ProofRepository repository;
    UserRepository userRepository;
    ProofMapper mapper;
    private SecurityServiceInterface securityService;
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public ProofPagePagination proofsPagination(int page, int size, boolean sort) {
        var pageRequest = repository.findAll(
                PageRequest.of(page, size, doDateSort(sort))
        );
        if (page >= pageRequest.getTotalPages())
            throw new PageNotFoundException(page);
        return mapper.toProofPagePagination(pageRequest);
    }

    @Override
    @Transactional
    public ProofEntity addProofProfile(long talentId, ProofAddRequest proofAddRequest) {
        return repository.save(ProofEntity.builder()
                .title(proofAddRequest.title())
                .description(proofAddRequest.description())
                .link(proofAddRequest.link())
                .status(Status.DRAFT)
                .dateCreated(Instant.now())
                .user(userRepository.findById(talentId)
                        .orElseThrow(() -> new TalentNotFoundException(talentId)))
                .build());
    }

    @Override
    public ResponseEntity<?> getLocation(long talentId, ProofAddRequest proofAddRequest) {
        long proofId = addProofProfile(talentId, proofAddRequest).getProofId();
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{proof-id}")
                .buildAndExpand(proofId)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @Override
    @Transactional
    public void deleteProof(long talentId, long proofId) {
        ProofEntity proof = em.find(ProofEntity.class,proofId);
        proof.setUser(null);
        em.remove(proof);
    }

    @Override
    @Transactional
    public ProofPagePagination getTalentAllProofs(Authentication auth, long talentId, int page, int size, boolean sort) {
        if (securityService.checkingLogged(talentId, auth)) {
            var pageRequest = repository.findByUser_UserId(talentId, PageRequest.of(page, size, doDateSort(sort)));
            if (page >= pageRequest.getTotalPages())
                throw new PageNotFoundException(page);
            return mapper.toProofPagePagination(pageRequest);
        }
        var pageRequest = repository.findByUser_UserIdAndStatus(talentId, Status.PUBLISHED, PageRequest.of(page, size, doDateSort(sort)));
        if (page >= pageRequest.getTotalPages())
            throw new PageNotFoundException(page);
        return mapper.toProofPagePagination(pageRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public ProofFullInfo getProofFullInfo(Authentication auth, long proofId) {
        if (!repository.existsByProofId(proofId)) {
            throw new ProofNotFoundException(proofId);
        }
        ProofEntity proof = em.find(ProofEntity.class,proofId);
        var talentId = proof.getUser().getUserId();
        if (securityService.checkingLogged(talentId, auth)) {
            var optionalProof = repository.findById(proofId);
            ProofEntity requestProof = optionalProof.orElseThrow(() -> new ProofNotFoundException(proofId));
            return mapper.toProofFullInfo(requestProof);
        }
        var optionalProof = repository.findByProofIdAndStatus(proofId, Status.PUBLISHED);
        ProofEntity requestProof = optionalProof.orElseThrow(() -> new ProofNotFoundException(proofId));
        return mapper.toProofFullInfo(requestProof);
    }

    private Sort doDateSort(boolean sort) {
        Sort dateSort;
        if (sort) {
            dateSort = Sort.by("dateCreated").descending();
        } else {
            dateSort = Sort.by("dateCreated");
        }
        return dateSort;
    }
}
