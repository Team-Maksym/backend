package starlight.backend.talent.service.mock;

import starlight.backend.talent.model.response.TalentPagePagination;
import starlight.backend.talent.service.TalentServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class TalentServiceMock implements TalentServiceInterface {
    @Override
    public TalentPagePagination talentPagination(int page, int size) {
        return null;
    }
}
