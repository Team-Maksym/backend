package starlight.backend.sponsor.service;

import org.springframework.security.core.Authentication;
import starlight.backend.sponsor.model.response.UnusableKudos;

public interface SponsorServiceInterface {
    UnusableKudos getUnusableKudos(long sponsorId);

    void deleteSponsor(long sponsorId, Authentication authentication);
}
