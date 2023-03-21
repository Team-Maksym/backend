package Strlight.backend.talent.service;

import Strlight.backend.talent.model.response.TalentPagePagination;

public interface TalentServiceInterface {
    TalentPagePagination talentPagination(int page, int size);
}