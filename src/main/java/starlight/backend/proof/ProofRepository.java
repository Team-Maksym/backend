package starlight.backend.proof;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import starlight.backend.proof.model.entity.ProofEntity;

@Repository
public interface ProofRepository extends JpaRepository<ProofEntity, Long> {

}