package starlight.backend.security.service;

import starlight.backend.talent.model.entity.TalentEntity;
import starlight.backend.talent.model.request.NewTalent;
import starlight.backend.talent.repository.TalentRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Primary
@AllArgsConstructor
@Service
@Transactional
public class TalentService {

    private TalentRepository repository;

    private PasswordEncoder passwordEncoder;

    public TalentEntity register(NewTalent newTalent) {
        if (!repository.existsByMail(newTalent.email()))
            return repository.save(TalentEntity.builder()
                    .fullName(newTalent.name())
                    .mail(newTalent.email())
                    .password(passwordEncoder.encode(newTalent.password()))
                    .authorities(List.of("ROLE_TALENT"))
                    .build());
        else
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Email '" + newTalent.email() + "' is already occupied");
    }
}
