package starlight.backend.proof;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import starlight.backend.proof.model.entity.ProofEntity;
import starlight.backend.proof.model.enums.Status;

import java.util.Optional;

@Repository
public interface ProofRepository extends JpaRepository<ProofEntity, Long> {
    Page<ProofEntity> findAllByUser_UserId(Long userId, Pageable pageable);

    boolean existsByProofId(Long proofId);

    Optional<ProofEntity> findByProofIdAndStatus(Long proofId, Status status);

    Page<ProofEntity> findByUser_UserIdAndStatus(Long userId, Status status, Pageable pageable);

    Page<ProofEntity> findByStatus(Status status, Pageable pageable);

    boolean existsByUser_UserIdAndProofId(Long userId, Long proofId);
}
