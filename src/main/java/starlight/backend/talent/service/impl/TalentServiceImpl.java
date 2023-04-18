package starlight.backend.talent.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import starlight.backend.exception.PageNotFoundException;
import starlight.backend.exception.TalentNotFoundException;
import starlight.backend.security.service.SecurityServiceInterface;
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
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional
public class TalentServiceImpl implements TalentServiceInterface {
    private MapperTalent mapper;
    private UserRepository repository;
    private PositionRepository positionRepository;
    private SecurityServiceInterface securityService;
    @PersistenceContext
    private EntityManager em;

    @Override
    public TalentPagePagination talentPagination(int page, int size) {
        var pageRequest = repository.findAll(
                PageRequest.of(page, size, Sort.by("userId").descending())
        );
        if (page >= pageRequest.getTotalPages())
            throw new PageNotFoundException(page);
        return mapper.toTalentPagePagination(pageRequest);
    }

    @Override
    public Optional<TalentFullInfo> talentFullInfo(long id) {
        return Optional.of(repository.findById(id)
                .map(mapper::toTalentFullInfo)
                .orElseThrow(() -> new TalentNotFoundException(id)));
    }

    @Override
    public TalentFullInfo updateTalentProfile(long id, TalentUpdateRequest talentUpdateRequest,Authentication auth) {
        if (!securityService.checkingLoggedAndToken(id, auth)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"you cannot change another talent");
        }
        var positions =findOrAddPositions(talentUpdateRequest);
        return repository.findById(id).map(talent -> {
            talent.setFullName(talentUpdateRequest.fullName());
            talent.setBirthday(talentUpdateRequest.birthday());
            talent.setAvatar(talentUpdateRequest.avatar());
            talent.setEducation(talentUpdateRequest.education());
            talent.setExperience(talentUpdateRequest.experience());
            talent.setPositions(positions);
            repository.save(talent);
            return mapper.toTalentFullInfo(talent);
        }).orElseThrow(() -> new TalentNotFoundException(id));
    }
    
    Set<PositionEntity> findOrAddPositions(TalentUpdateRequest talentUpdateRequest){
       return talentUpdateRequest.positions().stream()
               .map(position ->
                       positionRepository.findByPosition(position)
                               .orElse(new PositionEntity(position)))
               .collect(Collectors.toSet());
    }

    @Override
    public void deleteTalentProfile(long talentId, Authentication auth) {
        if (!securityService.checkingLoggedAndToken(talentId, auth)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"you cannot delete another talent");
        }
        UserEntity user = em.find(UserEntity.class, talentId);
        user.setPositions(null);
        em.remove(user);
    }
}

//    Set<PositionEntity> findOrAddPositions(TalentUpdateRequest talentUpdateRequest){
//        return talentUpdateRequest.positions().stream()
//                .map(position -> {
//                    PositionEntity p;
//                    if(positionRepository.existsByPosition(position)) {
//                        p = positionRepository.findByPosition(position);
//                    }else {
//                        p = new PositionEntity(position);
//                    }
//                    return p;
//                })
//                .collect(Collectors.toSet());
//
//    }