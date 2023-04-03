package starlight.backend.talent.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import starlight.backend.exception.PageNotFoundException;
import starlight.backend.exception.TalentNotFoundException;
import starlight.backend.talent.TalentMapper;
import starlight.backend.talent.model.response.TalentFullInfo;
import starlight.backend.talent.model.response.TalentPagePagination;
import starlight.backend.user.repository.UserRepository;
import starlight.backend.talent.service.TalentServiceInterface;

import java.util.Optional;

@AllArgsConstructor
@Service
@Transactional
public class TalentServiceImpl implements TalentServiceInterface {
    TalentMapper mapper;
    UserRepository repository;

    @Override
    public TalentPagePagination talentPagination(int page, int size) {
        var pageRequest = repository.findAll(PageRequest.of(page, size));
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
}
