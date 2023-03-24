package starlight.backend.security.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import starlight.backend.talent.model.entity.UserEntity;
import starlight.backend.talent.model.request.NewTalent;
import starlight.backend.talent.repository.UserRepository;

import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class TalentService {

    private UserRepository repository;

    private PasswordEncoder passwordEncoder;

    public UserEntity register(NewTalent newTalent) {
        if (!repository.existsByEmail(newTalent.email()))
            return repository.save(UserEntity.builder()
                    .fullName(newTalent.fullName())
                    .email(newTalent.email())
                    .password(passwordEncoder.encode(newTalent.password()))
                    .authorities(List.of("ROLE_TALENT"))
                    .build());
        else
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Email '" + newTalent.email() + "' is already occupied");
    }
}
