package Strlight.backend.talent.repository;

import Strlight.backend.talent.model.entity.TalentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface TalentRepository extends PagingAndSortingRepository<TalentEntity, Long>,
        JpaRepository<TalentEntity, Long> {
    Optional<TalentEntity> findByFullName(String fullName);

    Boolean existsByFullName(String username);

    Boolean existsByMail(String email);
}



