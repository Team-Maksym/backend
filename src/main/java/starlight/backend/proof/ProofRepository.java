package starlight.backend.proof;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import starlight.backend.proof.model.entity.ProofEntity;
import starlight.backend.proof.model.enums.Status;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProofRepository extends JpaRepository<ProofEntity, Long> {
    boolean existsByUser_UserIdAndSkills_SkillId(Long userId, Long skillId);
    List<ProofEntity> findByUser_UserIdAndSkills_SkillId(Long userId, Long skillId);
    List<ProofEntity> findByUser_UserIdAndSkills_SkillIdAndStatus(Long userId, Long skillId, Status status);
    List<ProofEntity> findByUser_UserIdAndStatus(Long userId, Status status);
    List<ProofEntity> findBySkills_SkillIdAndSkills_Talents_UserId(Long skillId, Long userId);
    Page<ProofEntity> findAllByUser_UserId(Long userId, Pageable pageable);

    boolean existsByProofId(Long proofId);

    Page<ProofEntity> findByUser_UserIdAndStatus(Long userId, Status status, Pageable pageable);

    Page<ProofEntity> findByStatus(Status status, Pageable pageable);

    boolean existsByUser_UserIdAndProofId(Long userId, Long proofId);

    Page<ProofEntity> findByUser_UserId(long talentId, PageRequest of);

    List<ProofEntity> findByUser_UserId(long talentId);
}
