package starlight.backend.talent.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import starlight.backend.exception.PageNotFoundException;
import starlight.backend.exception.TalentNotFoundException;
import starlight.backend.talent.MapperTalent;
import starlight.backend.talent.model.request.TalentUpdateRequest;
import starlight.backend.talent.model.response.TalentFullInfo;
import starlight.backend.talent.model.response.TalentPagePagination;
import starlight.backend.talent.service.TalentServiceInterface;
import starlight.backend.user.model.entity.PositionEntity;
import starlight.backend.user.model.entity.UserEntity;
import starlight.backend.user.repository.PositionRepository;
import starlight.backend.user.repository.UserRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TalentServiceImpl implements TalentServiceInterface {
    MapperTalent mapper;
    UserRepository repository;
    PositionRepository positionRepository;

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public TalentPagePagination talentPagination(int page, int size) {
        var pageRequest = repository.findAll(
                PageRequest.of(page, size, Sort.by("userId").descending())
        );
        if (page >= pageRequest.getTotalPages())
            throw new PageNotFoundException(page);
        return mapper.toTalentPagePagination(pageRequest);
    }

    @Override
    @Transactional
    public Optional<TalentFullInfo> talentFullInfo(long id) {
        return Optional.of(repository.findById(id)
                .map(mapper::toTalentFullInfo)
                .orElseThrow(() -> new TalentNotFoundException(id)));
    }

    @Override
    @Transactional
    public TalentFullInfo updateTalentProfile(long id, TalentUpdateRequest talentUpdateRequest) {
        return repository.findById(id).map(talent -> {
            talent.setFullName(talentUpdateRequest.fullName());
            talent.setBirthday(talentUpdateRequest.birthday());
            talent.setAvatar(talentUpdateRequest.avatar());
            talent.setEducation(talentUpdateRequest.education());
            talent.setExperience(talentUpdateRequest.experience());
            var positions = talentUpdateRequest.positions().stream()
                    .map(position ->
                            positionRepository.findByPosition(position)
                                    .orElse(new PositionEntity(position)))
                    .collect(Collectors.toSet());
            talent.setPositions(positions);
            repository.save(talent);
            return mapper.toTalentFullInfo(talent);
        }).orElseThrow(() -> new TalentNotFoundException(id));
    }

    @Override
    @Transactional
    public void deleteTalentProfile(long talentId) {
        UserEntity user = em.find(UserEntity.class, talentId);
        user.setPositions(null);
        em.remove(user);
    }
}
