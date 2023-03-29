package starlight.backend.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starlight.backend.user.model.entity.PositionEntity;

import java.util.Optional;


public interface PositionRepository  extends JpaRepository<PositionEntity, Long> {

   Optional<PositionEntity> findByPosition(String position);
}