package starlight.backend.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import starlight.backend.user.model.entity.PositionEntity;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PositionRepository  extends JpaRepository<PositionEntity, Long> {
  Optional<PositionEntity> findByPosition(String position);

//  PositionEntity findByPosition(String position);
//
//  boolean existsByPosition(String position);
}