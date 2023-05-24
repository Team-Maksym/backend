package starlight.backend.talent.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import starlight.backend.skill.model.response.SkillList;
import starlight.backend.talent.model.request.TalentUpdateRequest;
import starlight.backend.talent.model.response.TalentFullInfo;
import starlight.backend.talent.model.response.TalentPagePagination;

public interface TalentServiceInterface {
    TalentPagePagination talentPagination(int page, int size);

    TalentFullInfo talentFullInfo(long id);

    TalentFullInfo updateTalentProfile(long id, TalentUpdateRequest talentUpdateRequest, Authentication auth);

    void deleteTalentProfile(long talentId, Authentication auth);

    ResponseEntity<? extends Record> talentPaginationWithFilter(String filter, int skip, int limit);
}