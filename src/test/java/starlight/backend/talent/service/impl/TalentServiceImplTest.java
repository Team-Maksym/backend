package starlight.backend.talent.service.impl;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;
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

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
    private Authentication auth;
    @MockBean
    private EntityManager em;
    @Autowired
    private TalentServiceImpl talentService;
    private UserEntity user;

    @BeforeEach
    public void setup() {
        user = UserEntity.builder()
                .userId(1L)
                .fullName("Jon Snow")
                .email("myemail@gmail.com")
                .password("Secret123")
                .build();
    }

    @DisplayName("JUnit test for pagination (talents) method")
    @Test
    void talentPagination_WithValidPageNumber_ShouldGetPage() {
        // Given
        int page = 0;
        int size = 10;
        List<UserEntity> users = Arrays.asList(user, user);
        Page<UserEntity> pageRequest = new PageImpl<>(users);
        TalentPagePagination expectedPagination = TalentPagePagination.builder().build();
        given(repository.findAll(any(Pageable.class))).willReturn(pageRequest);
        given(mapper.toTalentPagePagination(pageRequest)).willReturn(expectedPagination);

        // When
        TalentPagePagination resultPagination = talentService.talentPagination(page, size);

        // Then
        assertThat(resultPagination).isEqualTo(expectedPagination);
    }

    @DisplayName("JUnit test for pagination (talents) method which throw exception")
    @Test
    void talentPagination_WithInvalidPageNumber_ShouldThrowPageNotFoundException() {
        // Given
        int page = 10;
        int size = 10;
        Page<UserEntity> pageRequest = new PageImpl<>(Collections.emptyList());
        given(repository.findAll(any(Pageable.class))).willReturn(pageRequest);

        // When // Then
        assertThatThrownBy(() -> talentService.talentPagination(page, size))
                .isInstanceOf(PageNotFoundException.class)
                .hasMessage("No such page " + page);
    }

    @DisplayName("JUnit test for get full info about talent method")
    @Test
    void talentFullInfo() {
        // Given
        TalentFullInfo expectedFullInfo = TalentFullInfo.builder().build();
        given(repository.findById(user.getUserId())).willReturn(Optional.of(user));
        given(mapper.toTalentFullInfo(user)).willReturn(expectedFullInfo);

        // When
        TalentFullInfo resultFullInfo = talentService.talentFullInfo(user.getUserId());

        // Then
        assertThat(resultFullInfo).isEqualTo(expectedFullInfo);
    }

    @DisplayName("JUnit test for get full info about talent method which throw exception")
    @Test
    void talentFullInfo_WithInvalidId_ShouldThrowTalentNotFoundException() {
        // Given
        long id = 1L;
        given(repository.findById(id)).willReturn(Optional.empty());

        // When // Then
        assertThatThrownBy(() -> talentService.talentFullInfo(id))
                .isInstanceOf(TalentNotFoundException.class)
                .hasMessage("Talent not found by id " + id);
    }

    @DisplayName("JUnit test for update info about talent method")
    @Test
    void updateTalentProfile() {
        // Given
        TalentUpdateRequest updateRequest = TalentUpdateRequest.builder()
                .fullName("Joly Moth")
                .password("Secret123")
                .birthday(LocalDate.of(1995, 1, 1))
                .avatar("https://example.com/new-avatar.jpg")
                .education("Master's Degree")
                .experience("5 years")
                .positions(Arrays.asList("Senior Software Engineer", "Team Lead"))
                .build();

        TalentFullInfo updatedTalentInfo = TalentFullInfo.builder().build();
        when(repository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(securityService.checkingLoggedAndToken(user.getUserId(), auth)).thenReturn(true);
        when(passwordEncoder.encode(updateRequest.password())).thenReturn(user.getPassword());
        when(mapper.toTalentFullInfo(any())).thenReturn(updatedTalentInfo);
        // When
        TalentFullInfo result = talentService.updateTalentProfile(user.getUserId(), updateRequest, auth);

        // Then
        assertEquals(updatedTalentInfo, result);
        assertEquals(updateRequest.password(), user.getPassword());
        if (updateRequest.fullName() != null) {
            assertEquals(updateRequest.fullName(), user.getFullName());
        } else {
            assertEquals("Jon Snow", user.getFullName());
        }
        if (updateRequest.positions() != null && !updateRequest.positions().isEmpty()) {
            assertEquals(updateRequest.positions(), user.getPositions().stream()
                    .map(PositionEntity::getPosition).collect(Collectors.toList()));
        } else {
            assertEquals(Arrays.asList("Senior Software Engineer", "Team Lead"), user.getPositions().stream()
                    .map(PositionEntity::getPosition).collect(Collectors.toList()));
        }
        assertNotEquals(updateRequest.avatar(), user.getAvatar());
        assertNotEquals(updateRequest.education(), user.getEducation());
        assertNotEquals(updateRequest.experience(), user.getExperience());

        verify(repository, times(1)).findById(user.getUserId());
    }

    @DisplayName("JUnit test for update info about talent method which throw exception")
    @Test
    void updateTalentProfile_WithInvalidId_ShouldThrowUnauthorizedException() {
        // Given
        TalentUpdateRequest updateRequest = TalentUpdateRequest.builder()
                .fullName("Jon Snow")
                .password("Secret123")
                .avatar("https://example.com/new-avatar.jpg")
                .education("New University")
                .experience("New Experience")
                .positions(Collections.singletonList("Manager"))
                .build();

        when(repository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(updateRequest.password())).thenReturn(user.getPassword());
        when(securityService.checkingLoggedAndToken(user.getUserId(), null)).thenReturn(false);

        // When // Then
        assertThatThrownBy(() -> talentService.updateTalentProfile(user.getUserId(), updateRequest, null))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("401 UNAUTHORIZED \"you cannot change another talent\"");
    }

    @DisplayName("JUnit test for delete talent method")
    @Test
    void deleteTalentProfile() {
        // Given
        when(em.find(UserEntity.class, user.getUserId())).thenReturn(user);
        when(securityService.checkingLoggedAndToken(user.getUserId(), auth)).thenReturn(true);

        // When
        talentService.deleteTalentProfile(user.getUserId(), auth);

        // Then
         verify(em, times(1)).remove(user);
    }

    @DisplayName("JUnit test for delete talent method which throw exception")
    @Test
    void deleteTalentProfile_WithInvalidId_ShouldThrowUnauthorizedException() {
        // Given
        when(em.find(UserEntity.class, user.getUserId())).thenReturn(user);
        when(securityService.checkingLoggedAndToken(user.getUserId(), null)).thenReturn(false);

        // When // Then
        assertThatThrownBy(() -> talentService.deleteTalentProfile(user.getUserId(), null))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("401 UNAUTHORIZED \"you cannot delete another talent\"");
    }
}