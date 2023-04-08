package starlight.backend.proof.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import starlight.backend.exception.PageNotFoundException;
import starlight.backend.exception.TalentNotFoundException;
import starlight.backend.proof.ProofMapper;
import starlight.backend.proof.ProofRepository;
import starlight.backend.proof.model.entity.ProofEntity;
import starlight.backend.proof.model.enums.Status;
import starlight.backend.proof.model.request.ProofAddRequest;
import starlight.backend.proof.model.response.ProofPagePagination;
import starlight.backend.proof.service.ProofServiceInterface;
import starlight.backend.user.repository.UserRepository;

import java.time.Instant;
import java.util.Objects;

@AllArgsConstructor
@Service
public class ProofServiceImpl implements ProofServiceInterface {
    ProofRepository repository;
    UserRepository userRepository;
    ProofMapper mapper;

    @Override
    @Transactional
    public ProofPagePagination proofsPagination(int page, int size, boolean sortDate) {
        Sort dateSort;
        if (sortDate) {
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

    private long getAddedProofId(long talentId, ProofAddRequest proofAddRequest) {
        return addProofProfile(talentId, proofAddRequest).getProofId();
    }

    @Override
    public long validationProofAdded(long talentId, ProofAddRequest proofAddRequest, Authentication auth) {
        if (auth != null && auth.isAuthenticated() &&
                (Objects.equals(auth.getName(), String.valueOf(talentId)))) {
            return getAddedProofId(talentId, proofAddRequest);
        } else if (!(Objects.equals(auth.getName(), String.valueOf(talentId)))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "you cannot change someone else's profile");

        } else if (!(auth != null && auth.isAuthenticated())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid credential");
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
