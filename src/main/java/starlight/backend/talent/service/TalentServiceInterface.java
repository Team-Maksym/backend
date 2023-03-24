package starlight.backend.talent.service;

import starlight.backend.talent.model.response.TalentFullInfo;
import starlight.backend.talent.model.response.TalentPagePagination;

import java.util.Optional;

public interface TalentServiceInterface {
    TalentPagePagination talentPagination(int page, int size);

    Optional<TalentFullInfo> talentFullInfo(long id);
}