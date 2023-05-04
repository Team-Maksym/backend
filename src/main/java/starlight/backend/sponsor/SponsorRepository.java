package starlight.backend.sponsor;

import org.springframework.data.jpa.repository.JpaRepository;
import starlight.backend.sponsor.model.entity.SponsorEntity;

import java.util.Optional;

public interface SponsorRepository extends JpaRepository<SponsorEntity, Long> {
    boolean existsBySponsorId(Long sponsorId);
    boolean existsByEmail(String email);

    Optional<SponsorEntity> findByEmail(String email);
}