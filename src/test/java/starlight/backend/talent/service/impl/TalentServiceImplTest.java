//package starlight.backend.talent.service.impl;
//
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.server.ResponseStatusException;
//import starlight.backend.exception.PageNotFoundException;
//import starlight.backend.exception.user.UserNotFoundException;
//import starlight.backend.exception.user.talent.TalentNotFoundException;
//import starlight.backend.proof.ProofRepository;
//import starlight.backend.proof.model.entity.ProofEntity;
//import starlight.backend.security.service.SecurityServiceInterface;
//import starlight.backend.talent.MapperTalent;
//import starlight.backend.talent.model.request.TalentUpdateRequest;
//import starlight.backend.talent.model.response.TalentFullInfo;
//import starlight.backend.talent.model.response.TalentPagePagination;
//import starlight.backend.user.model.entity.UserEntity;
//import starlight.backend.user.repository.PositionRepository;
//import starlight.backend.user.repository.UserRepository;
//
//import java.util.*;
//
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
//import static org.junit.Assert.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class TalentServiceImplTest {
//
//    @Mock
//    private MapperTalent mapper;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private PositionRepository positionRepository;
//
//    @Mock
//    private SecurityServiceInterface securityService;
//
//    @Mock
//    private ProofRepository proofRepository;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @InjectMocks
//    private TalentServiceImpl talentService;
//    private UserEntity user;
//
//    @BeforeEach
//    public void setup() {
//        user = UserEntity.builder()
//                .userId(1L)
//                .fullName("Jon Snow")
//                .email("myemail@gmail.com")
//                .password("Secret123")
//                .build();
//    }
//
//    @Test
//    @DisplayName("Test talentPagination: Valid page")
//    void testTalentPagination_ValidPage() {
//        // Given
//        int page = 0;
//        int size = 10;
//
//        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("userId").descending());
//        Page<UserEntity> userPage = new PageImpl<>(Collections.emptyList(), pageRequest, 20);
//
//        when(userRepository.findAll(pageRequest)).thenReturn(userPage);
//        when(mapper.toTalentPagePagination(userPage)).thenReturn(TalentPagePagination.builder().build());
//
//        // When
//        TalentPagePagination result = talentService.talentPagination(page, size);
//
//        // Then
//        assertNotNull(result);
//        verify(userRepository, times(1)).findAll(pageRequest);
//        verify(mapper, times(1)).toTalentPagePagination(userPage);
//    }
//
//    @Test
//    @DisplayName("Test talentPagination: Invalid page")
//    void testTalentPagination_InvalidPage() {
//        // Given
//        int page = 5;
//        int size = 10;
//
//        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("userId").descending());
//        Page<UserEntity> userPage = new PageImpl<>(Collections.emptyList(), pageRequest, 50);
//
//        when(userRepository.findAll(pageRequest)).thenReturn(userPage);
//
//        // When/Then
//        assertThrows(PageNotFoundException.class, () -> talentService.talentPagination(page, size));
//        verify(userRepository, times(1)).findAll(pageRequest);
//    }
//
//    @Test
//    @DisplayName("Test talentFullInfo: Existing talent")
//    void testTalentFullInfo_ExistingTalent() {
//        // Given
//        long id = 1L;
//        UserEntity userEntity = new UserEntity();
//        TalentFullInfo talentFullInfo = TalentFullInfo.builder().build();
//
//        when(userRepository.findById(id)).thenReturn(Optional.of(userEntity));
//        when(mapper.toTalentFullInfo(userEntity)).thenReturn(talentFullInfo);
//
//        // When
//        TalentFullInfo result = talentService.talentFullInfo(id);
//
//        // Then
//        assertEquals(talentFullInfo, result);
//        verify(userRepository, times(1)).findById(id);
//        verify(mapper, times(1)).toTalentFullInfo(userEntity);
//    }
//
//    @Test
//    @DisplayName("Test talentFullInfo: Non-existing talent")
//    void testTalentFullInfo_NonExistingTalent() {
//        // Given
//        long id = 1L;
//
//        when(userRepository.findById(id)).thenReturn(Optional.empty());
//
//        // When/Then
//        assertThrows(TalentNotFoundException.class, () -> talentService.talentFullInfo(id));
//        verify(userRepository, times(1)).findById(id);
//    }
//
//    @Test
//    @DisplayName("Test updateTalentProfile: Valid update")
//    void testUpdateTalentProfile_ValidUpdate() {
//        // Given
//        long id = 1L;
//        TalentUpdateRequest updateRequest = TalentUpdateRequest.builder().build();
//        Authentication authentication = Mockito.mock(Authentication.class);
//        UserEntity userEntity = new UserEntity();
//        userEntity.setUserId(id);
//
//        when(securityService.checkingLoggedAndToken(id, authentication)).thenReturn(true);
//        when(userRepository.findById(id)).thenReturn(Optional.of(userEntity));
//        when(mapper.toTalentFullInfo(userEntity)).thenReturn(TalentFullInfo.builder().build());
//
//        // When
//        TalentFullInfo result = talentService.updateTalentProfile(id, updateRequest, authentication);
//
//        // Then
//        assertNotNull(result);
//        verify(securityService, times(1)).checkingLoggedAndToken(id, authentication);
//        verify(userRepository, times(1)).findById(id);
//        verify(mapper, times(1)).toTalentFullInfo(userEntity);
//        verify(userRepository, times(1)).save(userEntity);
//    }
//
//    @Test
//    @DisplayName("Test updateTalentProfile: Unauthorized update")
//    void testUpdateTalentProfile_UnauthorizedUpdate() {
//        // Given
//        long id = 1L;
//        TalentUpdateRequest updateRequest = TalentUpdateRequest.builder().build();
//        Authentication authentication = Mockito.mock(Authentication.class);
//
//        when(securityService.checkingLoggedAndToken(id, authentication)).thenReturn(false);
//
//        // When/Then
//        assertThrows(ResponseStatusException.class, () -> talentService.updateTalentProfile(id, updateRequest, authentication));
//        verify(securityService, times(1)).checkingLoggedAndToken(id, authentication);
//        verify(userRepository, never()).findById(anyLong());
//        verify(mapper, never()).toTalentFullInfo(any(UserEntity.class));
//        verify(userRepository, never()).save(any(UserEntity.class));
//    }
//
//    @Test
//    @DisplayName("Test updateTalentProfile: Non-existing talent")
//    void testUpdateTalentProfile_NonExistingTalent() {
//        // Given
//        long id = 1L;
//        TalentUpdateRequest updateRequest = TalentUpdateRequest.builder().build();
//        Authentication authentication = Mockito.mock(Authentication.class);
//
//        when(securityService.checkingLoggedAndToken(id, authentication)).thenReturn(true);
//        when(userRepository.findById(id)).thenReturn(Optional.empty());
//
//        // When/Then
//        assertThrows(TalentNotFoundException.class, () -> talentService.updateTalentProfile(id, updateRequest, authentication));
//        verify(securityService, times(1)).checkingLoggedAndToken(id, authentication);
//        verify(userRepository, times(1)).findById(id);
//        verify(mapper, never()).toTalentFullInfo(any(UserEntity.class));
//        verify(userRepository, never()).save(any(UserEntity.class));
//    }
//
//    @DisplayName("Delete talent profile successfully")
//    @Test
//    void deleteTalentProfile() {
//        // Given
//        long talentId = 1L;
//        Authentication auth = Mockito.mock(Authentication.class);
//        given(userRepository.findById(user.getUserId())).willReturn(Optional.of(user));
//        when(securityService.checkingLoggedAndToken(user.getUserId(), auth)).thenReturn(true);
//        Set<ProofEntity> proofList = new HashSet<>();
//        ProofEntity proof = new ProofEntity();
//        proof.setProofId(1L);
//        proof.setUser(user);
//        proof.setKudos(new HashSet<>());
//        proofList.add(proof);
//        user.setProofs(proofList);
//        user.setAuthorities(new HashSet<>(Arrays.asList("TALENT_ROLE")));
//        when(proofRepository.findByUser_UserId(talentId)).thenReturn(Collections.emptyList());
//        doNothing().when(userRepository).deleteById(talentId);
//
//        // When
//        talentService.deleteTalentProfile(user.getUserId(), auth);
//
//        // Then
//        verify(securityService).checkingLoggedAndToken(user.getUserId(), auth);
//        verify(userRepository, times(1)).findById(user.getUserId());
//        assertTrue(user.getProofs().isEmpty());
//    }
//
//    @DisplayName("JUnit test for delete talent method which throw exception Unauthorized")
//    @Test
//    void deleteTalentProfile_WithInvalidId_ShouldThrowUnauthorizedException() {
//        // Given
//        when(securityService.checkingLoggedAndToken(user.getUserId(), null)).thenReturn(false);
//
//        // When // Then
//        assertThatThrownBy(() -> talentService.deleteTalentProfile(user.getUserId(), null))
//                .isInstanceOf(ResponseStatusException.class)
//                .hasMessage("401 UNAUTHORIZED \"you cannot delete another talent\"");
//    }
//
//    @Test
//    @DisplayName("Test deleteTalentProfile: Unauthorized deletion")
//    void testDeleteTalentProfile_UnauthorizedDeletion() {
//        // Given
//        long talentId = 1L;
//        Authentication authentication = Mockito.mock(Authentication.class);
//
//        when(securityService.checkingLoggedAndToken(talentId, authentication)).thenReturn(false);
//
//        // When/Then
//        assertThrows(ResponseStatusException.class, () -> talentService.deleteTalentProfile(talentId, authentication));
//        verify(securityService, times(1)).checkingLoggedAndToken(talentId, authentication);
//        verify(userRepository, never()).findById(anyLong());
//        verify(proofRepository, never()).findByUser_UserId(anyLong());
//        verify(userRepository, never()).deleteById(anyLong());
//    }
//
//    @Test
//    @DisplayName("Test deleteTalentProfile: Non-existing talent")
//    void testDeleteTalentProfile_NonExistingTalent() {
//        // Given
//        long talentId = 1L;
//        Authentication authentication = Mockito.mock(Authentication.class);
//
//        when(securityService.checkingLoggedAndToken(talentId, authentication)).thenReturn(true);
//        when(userRepository.findById(talentId)).thenReturn(Optional.empty());
//
//        // When/Then
//        assertThrows(UserNotFoundException.class, () -> talentService.deleteTalentProfile(talentId, authentication));
//        verify(securityService, times(1)).checkingLoggedAndToken(talentId, authentication);
//        verify(userRepository, times(1)).findById(talentId);
//        verify(proofRepository, never()).findByUser_UserId(anyLong());
//        verify(userRepository, never()).deleteById(anyLong());
//    }
//}