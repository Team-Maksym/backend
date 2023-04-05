package starlight.backend.proof.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import starlight.backend.exception.PageNotFoundException;
import starlight.backend.proof.ProofMapper;
import starlight.backend.proof.ProofRepository;
import starlight.backend.proof.model.response.ProofPagePagination;
import starlight.backend.proof.service.ProofServiceInterface;

@AllArgsConstructor
@Service
public class ProofServiceImpl implements ProofServiceInterface {
    ProofRepository repository;
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
}
