package Strlight.backend.talent.service.impl;

import Strlight.backend.talent.TalentMapper;
import Strlight.backend.talent.model.response.TalentFullInfo;
import Strlight.backend.talent.model.response.TalentPagePagination;
import Strlight.backend.talent.repository.TalentRepository;
import Strlight.backend.talent.service.TalentServiceInterface;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Primary
@AllArgsConstructor
@Service
public class TalentServiceImpl implements TalentServiceInterface {
    TalentMapper mapper;
    TalentRepository repository;

    @Override
    public TalentPagePagination talentPagination(int page, int size) {
        return mapper.toTalentPagePagination(repository.findAll(PageRequest.of(page, size)));
    }

    public Optional<TalentFullInfo> talentFullInfo(long id) {
        /*return repository.findById(id)
                .map(mapper::toTalentFullInfo)
                .orElseThrow(() -> new TalentNotFoundException(id));*/
        return repository.findById(id)
                .map(mapper::toTalentFullInfo);
    }
}
