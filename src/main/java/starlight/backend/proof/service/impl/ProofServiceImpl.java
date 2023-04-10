package starlight.backend.proof.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import starlight.backend.security.service.SecurityServiceInterface;
import starlight.backend.user.repository.UserRepository;

import java.net.URI;
import java.time.Instant;

@AllArgsConstructor
@Service
@Transactional
public class ProofServiceImpl implements ProofServiceInterface {
    private final String DATA_CREATED = "dateCreated";
    private ProofRepository repository;
    private UserRepository userRepository;
    private ProofMapper mapper;
    private SecurityServiceInterface securityService;
    @PersistenceContext
    private EntityManager em;

    @Override
    public ProofPagePagination proofsPagination(int page, int size, boolean sort) {
        var pageRequest = repository.findAll(
                PageRequest.of(page, size, doSort(sort, DATA_CREATED))
        );
        if (page >= pageRequest.getTotalPages())
            throw new PageNotFoundException(page);
        return mapper.toProofPagePagination(pageRequest);
    }

    @Override
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
    @Transactional(readOnly = true)
    public ResponseEntity<?> getLocation(long talentId,
                                         ProofAddRequest proofAddRequest,
                                         Authentication auth) {
        if (securityService.checkingLoggedAndToken(talentId, auth)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        long proofId = addProofProfile(talentId, proofAddRequest).getProofId();
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{proof-id}")
                .buildAndExpand(proofId)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @Override
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
    public void deleteProof(long talentId, long proofId) {
        ProofEntity proof = em.find(ProofEntity.class, proofId);
        proof.setUser(null);
        em.remove(proof);
    }

    @Override
    public ProofPagePagination getTalentAllProofs(Authentication auth, long talentId,
                                                  int page, int size, boolean sort) {
        if (securityService.checkingLogged(talentId, auth)) {
            var pageRequest = repository.findByUser_UserId(talentId,
                    PageRequest.of(page, size, doSort(sort, DATA_CREATED)));
            if (page >= pageRequest.getTotalPages())
                throw new PageNotFoundException(page);
            return mapper.toProofPagePagination(pageRequest);
        }
        var pageRequest = repository.findByUser_UserIdAndStatus(talentId,
                Status.PUBLISHED,
                PageRequest.of(page, size, doSort(sort, DATA_CREATED)));
        if (page >= pageRequest.getTotalPages())
            throw new PageNotFoundException(page);
        return mapper.toProofPagePagination(pageRequest);
    }

    @Transactional(readOnly = true)
    Sort doSort(boolean sort, String sortParam) {
        Sort dateSort = Sort.by(sortParam);
        if (sort) {
            dateSort.descending();
        }
        return dateSort;
    }
}
