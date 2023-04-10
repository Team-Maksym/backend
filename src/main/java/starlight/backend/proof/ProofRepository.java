package starlight.backend.proof;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import starlight.backend.proof.model.entity.ProofEntity;

@Repository
public interface ProofRepository extends JpaRepository<ProofEntity, Long> {
    Page<ProofEntity> findByUser_UserId(Long userId, Pageable pageable);

}
