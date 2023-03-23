package starlight.backend.talent.service;

import starlight.backend.talent.model.response.TalentPagePagination;

public interface TalentServiceInterface {
    TalentPagePagination talentPagination(int page, int size);
}