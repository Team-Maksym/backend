package starlight.backend.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starlight.backend.user.model.entity.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByName(String name);
}