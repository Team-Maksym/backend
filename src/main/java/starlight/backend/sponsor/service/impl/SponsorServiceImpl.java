package starlight.backend.sponsor.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import starlight.backend.exception.SponsorCanNotSeeAnotherSponsor;
import starlight.backend.exception.SponsorNotFoundException;
import starlight.backend.security.service.SecurityServiceInterface;
import starlight.backend.sponsor.SponsorMapper;
import starlight.backend.sponsor.SponsorRepository;
import starlight.backend.sponsor.model.request.SponsorUpdateRequest;
import starlight.backend.sponsor.model.response.SponsorFullInfo;
import starlight.backend.sponsor.model.response.UnusableKudos;
import starlight.backend.sponsor.service.SponsorServiceInterface;

@AllArgsConstructor
@Service
@Transactional
public class SponsorServiceImpl implements SponsorServiceInterface {
    private SponsorRepository sponsorRepository;
    private SecurityServiceInterface serviceService;
    private SponsorMapper mapper;

    @Override
    public UnusableKudos getUnusableKudos(long sponsorId) {
        var sponsor = sponsorRepository.findById(sponsorId)
                .orElseThrow(() -> new SponsorNotFoundException(sponsorId));
        return new UnusableKudos(sponsor.getUnusedKudos());
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
                .unusedKudos(sponsor.getUnusedKudos())
                .build();
    }

    private void isItMyAccount(long sponsorId, Authentication auth) {
        if (!serviceService.checkingLoggedAndToken(sponsorId, auth)) {
            throw new SponsorCanNotSeeAnotherSponsor();
        }
    }

    @Override
    public SponsorFullInfo updateSponsorProfile(long sponsorId,SponsorUpdateRequest sponsorUpdateRequest, Authentication auth) {
        if (!serviceService.checkingLoggedAndToken(sponsorId, auth)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you cannot change another sponsor");
        }
        return sponsorRepository.findById(sponsorId).map(sponsor -> {
                    sponsor.setAvatar(validationField(
                            sponsorUpdateRequest.avatar(),
                            sponsor.getAvatar()));
                    sponsor.setCompany(validationField(
                            sponsorUpdateRequest.company(),
                            sponsor.getCompany()));
                    sponsor.setFullName(validationField(
                            sponsorUpdateRequest.fullName(),
                            sponsor.getFullName()));
                    sponsor.setUnusedKudos(
                            sponsorUpdateRequest.unusedKudos() == 0 ?
                                    sponsor.getUnusedKudos() :
                                    sponsorUpdateRequest.unusedKudos());
                    sponsorRepository.save(sponsor);
                    return mapper.toSponsorFullInfo(sponsor);
                })
                .orElseThrow(() -> new SponsorNotFoundException(sponsorId));
    }

    private String validationField(String newParam, String lastParam) {
        return newParam == null ?
                lastParam :
                newParam;
    }
}
