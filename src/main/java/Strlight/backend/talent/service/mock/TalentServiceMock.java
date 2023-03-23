package Strlight.backend.talent.service.mock;

import Strlight.backend.talent.model.response.TalentFullInfo;
import Strlight.backend.talent.model.response.TalentPagePagination;
import Strlight.backend.talent.service.TalentServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@Service
public class TalentServiceMock implements TalentServiceInterface {
    @Override
    public TalentPagePagination talentPagination(int page, int size) {
        return null;
    }

    @Override
    public Optional<TalentFullInfo> talentFullInfo(long id) {
        return null;
    }
}
