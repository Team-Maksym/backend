package starlight.backend.talent.service;

import starlight.backend.talent.model.response.TalentFullInfo;
import starlight.backend.talent.model.response.TalentPagePagination;
import starlight.backend.talent.model.request.TalentUpdateRequest;

import java.util.Optional;

public interface TalentServiceInterface {
    TalentPagePagination talentPagination(int page, int size);

    Optional<TalentFullInfo> talentFullInfo(long id);

    TalentFullInfo updateTalentProfile(long id, TalentUpdateRequest talentUpdateRequest);

    void deleteTalentProfile(long talentId);
}