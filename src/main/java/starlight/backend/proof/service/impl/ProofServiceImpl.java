package starlight.backend.proof.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
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
import starlight.backend.proof.model.request.ProofUpdateRequest;
import starlight.backend.proof.model.response.ProofFullInfo;
import starlight.backend.proof.model.response.ProofPagePagination;
import starlight.backend.proof.service.ProofServiceInterface;
import starlight.backend.user.model.entity.UserEntity;
import starlight.backend.user.repository.UserRepository;

import java.net.URI;
import java.time.Instant;
import java.util.Objects;

@AllArgsConstructor
@Service
public class ProofServiceImpl implements ProofServiceInterface {
    ProofRepository repository;
    UserRepository userRepository;
    ProofMapper mapper;
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public ProofPagePagination proofsPagination(int page, int size, boolean sort) {
        Sort dateSort;
        if (sort) {
            dateSort = Sort.by("dateCreated").descending();
        } else {
            dateSort = Sort.by("dateCreated");
        }
        var pageRequest = repository.findAll(
                PageRequest.of(page, size, dateSort)
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
    public ProofFullInfo proofUpdateRequest(long id, ProofUpdateRequest proofUpdateRequest) {
        return repository.findById(id).map(proof -> {
            proof.setTitle(proofUpdateRequest.title());
            proof.setDescription(proofUpdateRequest.description());
            proof.setLink(proofUpdateRequest.link());
            proof.setDateLastUpdated(Instant.now());
            repository.save(proof);
            return mapper.toProofFullInfo(proof);
        }).orElseThrow(() -> new ProofNotFoundException(id));
    }

    @Override
    @Transactional
    public void deleteProof(long talentId, long proofId) {
        ProofEntity proof = em.find(ProofEntity.class,proofId);
        proof.setUser(null);
        em.remove(proof);
    }
}
