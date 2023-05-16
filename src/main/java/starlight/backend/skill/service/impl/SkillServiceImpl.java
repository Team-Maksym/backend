package starlight.backend.skill.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import starlight.backend.exception.proof.ProofNotFoundException;
import starlight.backend.exception.proof.UserAccesDeniedToProofException;
import starlight.backend.exception.proof.UserCanNotEditProofNotInDraftException;
import starlight.backend.proof.ProofRepository;
import starlight.backend.proof.model.enums.Status;
import starlight.backend.security.service.SecurityServiceInterface;
import starlight.backend.skill.SkillMapper;
import starlight.backend.skill.model.entity.SkillEntity;
import starlight.backend.skill.model.request.AddSkill;
import starlight.backend.skill.model.response.ProofWithSkills;
import starlight.backend.skill.model.response.SkillList;
import starlight.backend.skill.model.response.SkillListWithPagination;
import starlight.backend.skill.repository.SkillRepository;
import starlight.backend.skill.service.SkillServiceInterface;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional
public class SkillServiceImpl implements SkillServiceInterface {
    private final String filterParam = "skill";
    private SkillRepository skillRepository;
    private SkillMapper skillMapper;
    private SecurityServiceInterface securityService;
    private ProofRepository proofRepository;

    @Override
    public SkillListWithPagination getListSkillWithFiltration(String filter, int skip, int limit) {
        var skillStream = skillRepository.findAll().stream();

        if (filter != null && !filter.isEmpty()) {
            skillStream = skillStream.filter(skill -> skill.getSkill().contains(filter));
        }
        Sort sort = Sort.by(Sort.Order.asc(filterParam));

        var pageable = PageRequest.of(skip, limit, sort);

        List<SkillEntity> sortedSkills = skillStream
                .sorted(Comparator.comparing(SkillEntity::getSkill))
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .toList();
        return skillMapper.toSkillListWithPagination(sortedSkills, skillRepository.count());
    }

}
