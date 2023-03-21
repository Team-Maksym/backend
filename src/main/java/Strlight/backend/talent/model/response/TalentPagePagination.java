package Strlight.backend.talent.model.response;

import lombok.Builder;

import java.util.List;

@Builder
public record TalentPagePagination(
       int totalPage,
       long totalTalents,
       List<TalentProfile> talentProfileList
) {}
