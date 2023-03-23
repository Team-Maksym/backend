package Strlight.backend.talent.service;

import Strlight.backend.talent.model.response.TalentFullInfo;
import Strlight.backend.talent.model.response.TalentPagePagination;

import java.util.Optional;

public interface TalentServiceInterface {
    TalentPagePagination talentPagination(int page, int size);

    Optional<TalentFullInfo> talentFullInfo(long id);
}