package starlight.backend.talent.service.impl;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import starlight.backend.exception.PageNotFoundException;
import starlight.backend.exception.TalentNotFoundException;
import starlight.backend.security.service.SecurityServiceInterface;
import starlight.backend.talent.MapperTalent;
import starlight.backend.talent.model.request.TalentUpdateRequest;
import starlight.backend.talent.model.response.TalentFullInfo;
import starlight.backend.talent.model.response.TalentPagePagination;
import starlight.backend.user.model.entity.PositionEntity;
import starlight.backend.user.model.entity.UserEntity;
import starlight.backend.user.repository.PositionRepository;
import starlight.backend.user.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class TalentServiceImplTest {
    @MockBean
    private MapperTalent mapper;

    @MockBean
    private UserRepository repository;

    @MockBean
    private PositionRepository positionRepository;

    @MockBean
    private SecurityServiceInterface securityService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private EntityManager em;

    @Autowired
    private TalentServiceImpl talentService;


    @Test
    void talentPagination_WithValidPageNumber_ShouldGetPage() {
        // Given
        int page = 0;
        int size = 10;
        List<UserEntity> users = Arrays.asList(new UserEntity(), new UserEntity());
        Page<UserEntity> pageRequest = new PageImpl<>(users);
        TalentPagePagination expectedPagination = TalentPagePagination.builder().build();
        given(repository.findAll(any(Pageable.class))).willReturn(pageRequest);
        given(mapper.toTalentPagePagination(pageRequest)).willReturn(expectedPagination);

        // When
        TalentPagePagination resultPagination = talentService.talentPagination(page, size);

        // Then
        assertThat(resultPagination).isEqualTo(expectedPagination);
    }

    @Test
    void talentPagination_WithInvalidPageNumber_ShouldThrowPageNotFoundException() {
        // Given
        int page = 10;
        int size = 10;
        Page<UserEntity> pageRequest = new PageImpl<>(Collections.emptyList());
        given(repository.findAll(any(Pageable.class))).willReturn(pageRequest);

        // Then
        assertThatThrownBy(() -> talentService.talentPagination(page, size))
                .isInstanceOf(PageNotFoundException.class)
                .hasMessage("No such page " + page);
    }

    @Test
    void talentFullInfo() {
        // Given
        UserEntity user = new UserEntity();
        TalentFullInfo expectedFullInfo = TalentFullInfo.builder().build();
        given(repository.findById(user.getUserId())).willReturn(Optional.of(user));
        given(mapper.toTalentFullInfo(user)).willReturn(expectedFullInfo);

        // When
        Optional<TalentFullInfo> resultFullInfo = talentService.talentFullInfo(user.getUserId());

        // Then
        assertThat(resultFullInfo).isPresent().contains(expectedFullInfo);
    }

    @Test
    void talentFullInfo_WithInvalidId_ShouldThrowTalentNotFoundException() {
        // Given
        long id = 1L;
        given(repository.findById(id)).willReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> talentService.talentFullInfo(id))
                .isInstanceOf(TalentNotFoundException.class)
                .hasMessage("Talent not found by id " + id);
    }

    @Test
    void updateTalentProfile() {
        // Given
        UserEntity user = new UserEntity();
        user.setFullName("John Doe");
        user.setPassword("password");
        user.setAvatar("https://example.com/avatar.jpg");
        user.setEducation("Some University");
        user.setExperience("Some Experience");
        Set<PositionEntity> positions = new HashSet<>();
        positions.add(new PositionEntity("Developer"));
        user.setPositions(positions);
        TalentUpdateRequest updateRequest = TalentUpdateRequest.builder()
                .fullName("Jane Doe")
                .avatar("https://example.com/new-avatar.jpg")
                .education("New University")
                .password("newpassword")
                .experience("New Experience")
                .positions(Collections.singletonList("Manager"))
                .build();

        List<String> updatedPositions = new ArrayList<>();
        updatedPositions.add("Manager");

        TalentFullInfo updatedTalentInfo = TalentFullInfo.builder()
                .fullName(updateRequest.fullName())
                .avatar(updateRequest.avatar())
                .education(updateRequest.education())
                .experience(updateRequest.experience())
                .positions(updatedPositions)
                .build();
        when(repository.findById(user.getUserId())).thenReturn(Optional.of(user));
        //when(securityService.checkingLoggedAndToken(user.getUserId(), null)).thenReturn(false);
        when(passwordEncoder.encode(updateRequest.password())).thenReturn("hashedpassword");
        when(mapper.toTalentFullInfo(any())).thenReturn(updatedTalentInfo);

        // When
        //TalentFullInfo result = talentService.updateTalentProfile(user.getUserId(), updateRequest, null);

        // Then
       // assertEquals(updatedTalentInfo, result);
        assertEquals(updateRequest.fullName(), user.getFullName());
        assertEquals("hashedpassword", user.getPassword());
        assertEquals(updateRequest.avatar(),user.getAvatar());
        assertEquals(updateRequest.education(),user.getEducation());
        assertEquals(updateRequest.experience(), user.getExperience());
        assertEquals(updatedPositions, user.getPositions().stream()
                .map(PositionEntity::getPosition).collect(Collectors.toSet()));
        verify(repository, times(1)).findById(user.getUserId());
    }

    @Test
    void deleteTalentProfile() {
        // Given
        UserEntity user = new UserEntity();
        user.setFullName("testuser");
        user.setEmail("zaxaqueiboigreu-5997@gmail.com");
        user.setPassword("password");
        when(em.find(UserEntity.class, user.getUserId())).thenReturn(user);
       // when(securityService.checkingLoggedAndToken(user.getUserId(), null)).thenReturn(true);

        // When
        //talentService.deleteTalentProfile(user.getUserId(), null);

        // Then
        verify(em, times(1)).remove(user);
    }
}