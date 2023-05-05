package starlight.backend.advice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import starlight.backend.advice.model.entity.DelayedDeleteEntity;

@Repository
public interface DelayedDeleteRepository extends JpaRepository<DelayedDeleteEntity, Long> {

}
