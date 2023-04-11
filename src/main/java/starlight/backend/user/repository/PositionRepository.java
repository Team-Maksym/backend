package starlight.backend.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import starlight.backend.user.model.entity.PositionEntity;

import java.util.Optional;

@Repository
public interface PositionRepository  extends JpaRepository<PositionEntity, Long> {
   Optional<PositionEntity> findByPosition(String position);
}