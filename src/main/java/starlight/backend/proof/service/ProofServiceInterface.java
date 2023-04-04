package starlight.backend.proof.service;

import starlight.backend.proof.model.response.ProofPagePagination;

public interface ProofServiceInterface {
    ProofPagePagination proofsPagination(int page, int size,boolean ascending);
}
