package starlight.backend.advice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import starlight.backend.advice.model.entity.DelayedDeleteEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DelayedDeleteRepository extends JpaRepository<DelayedDeleteEntity, Long> {
    Optional<DelayedDeleteEntity> findByUserDeletingProcessUUID(UUID userDeletingProcessUUID);

    boolean existsByEntityID(Long entityID);
    
}
