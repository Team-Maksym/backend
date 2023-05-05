package starlight.backend.sponsor.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import starlight.backend.exception.SponsorCanNotSeeAnotherSponsor;
import starlight.backend.exception.SponsorNotFoundException;
import starlight.backend.security.service.SecurityServiceInterface;
import starlight.backend.sponsor.SponsorMapper;
import starlight.backend.sponsor.SponsorRepository;
import starlight.backend.sponsor.model.response.KudosWithProofId;
import starlight.backend.sponsor.model.response.SponsorFullInfo;
import starlight.backend.sponsor.model.response.UnusableKudos;
import starlight.backend.sponsor.service.SponsorServiceInterface;

import java.util.List;

@AllArgsConstructor
@Service
@Transactional
@Slf4j
public class SponsorServiceImpl implements SponsorServiceInterface {
    private SponsorRepository sponsorRepository;
    private SecurityServiceInterface serviceService;
    private SponsorMapper sponsorMapper;
    @Override
    public UnusableKudos getUnusableKudos(long sponsorId, Authentication auth) {
        isItMyAccount(sponsorId, auth);
        var sponsor = sponsorRepository.findById(sponsorId)
                .orElseThrow(() -> new SponsorNotFoundException(sponsorId));
        List<KudosWithProofId> kudosList = sponsor.getKudos()
                .stream()
                .map(el -> sponsorMapper.toKudosWithProofId(el))
                .toList();
        return new UnusableKudos(sponsor.getUnusedKudos(), kudosList);
    }

    @Override
    public SponsorFullInfo getSponsorFullInfo(long sponsorId, Authentication auth) {
        var sponsor = sponsorRepository.findById(sponsorId)
                .orElseThrow(() -> new SponsorNotFoundException(sponsorId));
        isItMyAccount(sponsorId, auth);
        return SponsorFullInfo.builder()
                .fullName(sponsor.getFullName())
                .avatar(sponsor.getAvatar())
                .company(sponsor.getCompany())
                .build();
    }

    private void isItMyAccount(long sponsorId, Authentication auth) {
        if (!serviceService.checkingLoggedAndToken(sponsorId, auth)) {
            throw new SponsorCanNotSeeAnotherSponsor();
        }
    }
}
