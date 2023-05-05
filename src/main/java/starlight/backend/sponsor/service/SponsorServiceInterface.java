package starlight.backend.sponsor.service;

import starlight.backend.sponsor.model.response.UnusableKudos;

public interface SponsorServiceInterface {
    UnusableKudos getUnusableKudos(long sponsorId);
}
