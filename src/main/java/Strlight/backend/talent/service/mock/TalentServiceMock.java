package Strlight.backend.talent.service.mock;

import Strlight.backend.talent.model.response.TalentPagePagination;
import Strlight.backend.talent.service.TalentServiceInterface;
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
