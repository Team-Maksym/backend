package starlight.backend.talent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import starlight.backend.talent.model.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long>,
        JpaRepository<UserEntity, Long> {

    Boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);

}
