package starlight.backend.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.config.annotation.web.PortMapperDsl;
import starlight.backend.user.model.entity.SponsorEntity;

import java.util.Optional;

public interface SponsorRepository extends JpaRepository<SponsorEntity, Long> {
    boolean existsByEmail(String email);

    Optional<SponsorEntity> findByEmail(String email);
}