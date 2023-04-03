package starlight.backend.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import starlight.backend.user.model.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);
}
