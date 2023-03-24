package starlight.backend.talent.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import starlight.backend.talent.TalentMapper;
import starlight.backend.talent.model.response.TalentFullInfo;
import starlight.backend.talent.model.response.TalentPagePagination;
import starlight.backend.talent.repository.UserRepository;
import starlight.backend.talent.service.TalentServiceInterface;

import java.util.Optional;

@Primary
@AllArgsConstructor
@Service
public class TalentServiceImpl implements TalentServiceInterface {
    TalentMapper mapper;
    UserRepository repository;
    PasswordEncoder passwordEncoder;

    @Override
    public TalentPagePagination talentPagination(int page, int size) {
        return mapper.toTalentPagePagination(
                repository.findAll(PageRequest.of(page, size))
        );
    }

    public Optional<TalentFullInfo> talentFullInfo(long id) {
        return repository.findById(id)
                .map(mapper::toTalentFullInfo);
                /*.orElseThrow(() -> new TalentNotFoundException(id));*/

    }
}
