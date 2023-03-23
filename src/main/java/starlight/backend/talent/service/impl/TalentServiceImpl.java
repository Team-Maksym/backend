package starlight.backend.talent.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import starlight.backend.talent.TalentMapper;
import starlight.backend.talent.model.response.TalentPagePagination;
import starlight.backend.talent.repository.TalentRepository;
import starlight.backend.talent.service.TalentServiceInterface;

@Primary
@AllArgsConstructor
@Service
public class TalentServiceImpl implements TalentServiceInterface {
    TalentMapper mapper;
    TalentRepository repository;
    PasswordEncoder passwordEncoder;

    @Override
    public TalentPagePagination talentPagination(int page, int size) {
        return mapper.toTalentPagePagination(
                repository.findAll(PageRequest.of(page, size))
        );
    }
}
