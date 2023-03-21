package Strlight.backend.talent.service;

import Strlight.backend.talent.model.response.TalentPagePagination;
import org.springframework.stereotype.Service;

@Service
public interface TalentServiceInterface {
    public TalentPagePagination talentPagination(int page, int size);
}