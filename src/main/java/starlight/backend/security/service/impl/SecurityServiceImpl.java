package starlight.backend.security.service.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import starlight.backend.exception.EmailAlreadyOccupiedException;
import starlight.backend.exception.user.sponsor.SponsorNotFoundException;
import starlight.backend.security.MapperSecurity;
import starlight.backend.security.model.UserDetailsImpl;
import starlight.backend.security.model.request.NewUser;
import starlight.backend.security.model.response.SessionInfo;
import starlight.backend.security.service.SecurityServiceInterface;
import starlight.backend.sponsor.SponsorRepository;
import starlight.backend.sponsor.model.entity.SponsorEntity;
import starlight.backend.sponsor.model.enums.SponsorStatus;
import starlight.backend.talent.model.entity.TalentEntity;
import starlight.backend.talent.repository.TalentRepository;
import starlight.backend.user.model.entity.RoleEntity;
import starlight.backend.user.model.entity.UserEntity;
import starlight.backend.user.model.enums.Role;
import starlight.backend.user.repository.RoleRepository;
import starlight.backend.user.repository.UserRepository;

import java.time.Instant;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.MINUTES;

@AllArgsConstructor
@Service
@Transactional
@Slf4j
public class SecurityServiceImpl implements SecurityServiceInterface {
    private final JwtEncoder jwtEncoder;
    private TalentRepository talentRepository;
    private SponsorRepository sponsorRepository;
    private MapperSecurity mapperSecurity;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Override
    public SessionInfo loginInfo(Authentication auth) {
        if (talentRepository.existsByEmail(auth.getName())) {
            var user = userRepository.findByTalent_Email(auth.getName());
            var token = getJWTToken(mapperSecurity.toUserDetailsImplTalent(user),
                    user.getTalent().getTalentId());
            return mapperSecurity.toSessionInfo(token);
        }
        var user = userRepository.findBySponsor_Email(auth.getName());
        var token = getJWTToken(mapperSecurity.toUserDetailsImplSponsor(user),
                user.getSponsor().getSponsorId());
        return mapperSecurity.toSessionInfo(token);
    }

    @Override
    public boolean isSponsorActive(Authentication auth) {
        for (GrantedAuthority grantedAuthority : auth.getAuthorities()) {
            if (grantedAuthority.getAuthority().equals(Role.SPONSOR.getAuthority())) {
                var sponsor = sponsorRepository.findById(Long.valueOf(auth.getName()))
                        .orElseThrow(() -> new SponsorNotFoundException(Long.parseLong(auth.getName())));
                return sponsor.getStatus().equals(SponsorStatus.ACTIVE);
            }
        }
        return true;
    }

    @Override
    public SessionInfo register(NewUser newUser) {
        var user = saveNewUser(newUser);
        if (talentRepository.existsByEmail(newUser.email())) {
            var token = getJWTToken(mapperSecurity.toUserDetailsImplTalent(user),
                    user.getTalent().getTalentId());
            return mapperSecurity.toSessionInfo(token);
        }
        var token = getJWTToken(mapperSecurity.toUserDetailsImplSponsor(user),
                user.getSponsor().getSponsorId());
        return mapperSecurity.toSessionInfo(token);
    }

    UserEntity saveNewUser(NewUser newUser) {
        if (talentRepository.existsByEmail(newUser.email()) ||
                sponsorRepository.existsByEmail(newUser.email())) {
            throw new EmailAlreadyOccupiedException(newUser.email());
        }

        if (newUser.role().equals(Role.SPONSOR.toString())) {
            var sponsor = sponsorRepository.save(SponsorEntity.builder()
                    .fullName(newUser.fullName())
                    .email(newUser.email())
                    .password(passwordEncoder.encode(newUser.password()))
                    .unusedKudos(100) //TODO не хадкодить
                    .status(SponsorStatus.ACTIVE)
                    .build());
            var role = roleRepository.findByName(Role.SPONSOR.getAuthority());
            return userRepository.save(UserEntity.builder()
                    .role(role)
                    .sponsor(sponsor)
                    .build());
        }
        var talent = talentRepository.save(TalentEntity.builder()
                .fullName(newUser.fullName())
                .email(newUser.email())
                .password(passwordEncoder.encode(newUser.password()))
                .build());
        var role = roleRepository.findByName(Role.TALENT.getAuthority());
        return userRepository.save(UserEntity.builder()
                .role(role)
                .talent(talent)
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public String getJWTToken(UserDetailsImpl authentication, long id) {
        var now = Instant.now();
        var claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(180, MINUTES))
                .subject(String.valueOf(id))
                .claim("scope", createScope(authentication))
                .claim("status", authentication.getStatus())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Override
    @Transactional(readOnly = true)
    public String createScope(UserDetailsImpl authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
    }
}
