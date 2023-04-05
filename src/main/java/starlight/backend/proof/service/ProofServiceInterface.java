package starlight.backend.proof.service;

import org.springframework.security.core.Authentication;
import starlight.backend.proof.model.entity.ProofEntity;
import starlight.backend.proof.model.request.ProofAddRequest;
import starlight.backend.proof.model.response.ProofPagePagination;

public interface ProofServiceInterface {
    ProofPagePagination proofsPagination(int page, int size, boolean sortDate);

    ProofEntity addProofProfile(long talentId, ProofAddRequest proofUpdateRequest);

    long validationProofAdded(long talentId, ProofAddRequest proofAddRequest, Authentication auth);


}
