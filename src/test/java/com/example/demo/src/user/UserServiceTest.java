package com.example.demo.src.user;

import com.example.demo.common.enums.UserState;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.user.entity.User;
import com.example.demo.src.user.model.PostUserReq;
import com.example.demo.src.user.model.PostUserRes;
import com.example.demo.utils.SHA256;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
class UserServiceTest {


    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("유저 생성 성공 테스트")
    void createUser_success() throws Exception {
        // 입력 데이터 준비
        LocalDate testBirthDate = LocalDate.of(1990, 1, 1);
        String rawPassword = "password1";
        String encryptedPassword = SHA256.encrypt(rawPassword); // 비밀번호를 미리 암호화

        PostUserReq postUserReq = new PostUserReq("user@example.com", encryptedPassword, "Test User", testBirthDate, true, true, true);


        // 모킹: 사용자 중복 체크 (없음을 가정)
        when(userRepository.findByEmailAndState(postUserReq.getEmail(), UserState.ACTIVE)).thenReturn(Optional.empty());

        // 사용자 저장을 위한 준비
        User preparedUser = postUserReq.toEntity();
        when(userRepository.save(any(User.class))).thenReturn(preparedUser);

        // 메소드 실행
        PostUserRes postUserRes = userService.createUser(postUserReq);

        // Then - 검증
        assertNotNull(postUserRes, "PostUserRes는 null이 아니어야 합니다.");
        assertEquals(preparedUser.getId(), postUserRes.getId(), "생성된 사용자 ID는 저장된 User 객체의 ID와 일치해야 합니다.");

        // UserRepository 메소드 호출 검증
        verify(userRepository).findByEmailAndState(postUserReq.getEmail(), UserState.ACTIVE);
        verify(userRepository).save(any(User.class));

        // 상태 검증
        assertEquals(postUserReq.getEmail(), preparedUser.getEmail(), "이메일이 일치해야 합니다.");
        assertEquals(encryptedPassword, preparedUser.getPassword(), "비밀번호가 암호화되어 저장되어야 합니다.");
        assertEquals(postUserReq.getName(), preparedUser.getName(), "이름이 일치해야 합니다.");
        assertEquals(postUserReq.isPrivacyPolicyAgreed(), preparedUser.isPrivacyPolicyAgreed(), "개인정보 처리방침 동의 상태가 일치해야 합니다.");
    }

    @Test
    @DisplayName("유저 개인정보 처리방침 동의 없는 경우")
    void createUser_PrivacyPolicyNotAgreed_ThrowsException() {
        // Given
        LocalDate testBirthDate = LocalDate.of(1990, 1, 1); // 테스트용 생년월일
        PostUserReq postUserReq = new PostUserReq("user@example.com", "password", "Test User", testBirthDate, false, true, true);

        // When & Then
        assertThrows(BaseException.class, () -> userService.createUser(postUserReq), "개인정보 처리방침에 대한 동의가 필요합니다.");
    }

    @Test
    @DisplayName("데이터 정책에 대한 동의 없는 경우")
    void createUserLOCATION_DATA_POLICY_AGREEMENT_ThrowsException() {
        // Given
        LocalDate testBirthDate = LocalDate.of(1990, 1, 1); // 테스트용 생년월일
        PostUserReq postUserReq = new PostUserReq("user@example.com", "password", "Test User", testBirthDate, true, true, false);

        // When & Then
        assertThrows(BaseException.class, () -> userService.createUser(postUserReq), "개인정보 처리방침에 대한 동의가 필요합니다.");
    }

    @Test
    @DisplayName("위치기반 서비스에 대한 동의  없는 경우")
    void createUser_LOCATION_BASED_SERVICES_ThrowsException() {
        // Given
        LocalDate testBirthDate = LocalDate.of(1990, 1, 1); // 테스트용 생년월일
        PostUserReq postUserReq = new PostUserReq("user@example.com", "password", "Test User", testBirthDate, true, false, true);

        // When & Then
        assertThrows(BaseException.class, () -> userService.createUser(postUserReq), "개인정보 처리방침에 대한 동의가 필요합니다.");
    }


    @Test
    @DisplayName("이미 존재하는 이메일로 사용자 생성 시도")
    void createUser_ExistingEmail_ThrowsException() {
        // Given
        LocalDate testBirthDate = LocalDate.of(1990, 1, 1); // 테스트용 생년월일
        PostUserReq secondUserReq = new PostUserReq("user@example.com", "password", "Test User", testBirthDate, true, true, true);


        User user = User.userBuilder()
                .email("example@example.com")
                .password("password")
                .name("Test User")
                .isOAuth(false)
                .privacyPolicyAgreed(true)
                .locationBasedServicesAgreed(true) // 누락된 필드 추가
                .dataPolicyAgreed(true) // 누락된 필드 추가
                .build();
        // 이미 존재하는 사용자를 나타내기 위해 userRepository.findByEmailAndState 모킹
        when(userRepository.findByEmailAndState(secondUserReq.getEmail(), UserState.ACTIVE))
                .thenReturn(Optional.of(user)); // 수정된 부분: User 객체는 빌더를 사용하여 생성된 객체를 사용
        // When & Then
        BaseException thrown = assertThrows(BaseException.class, () -> userService.createUser(secondUserReq));
        // 예외 메시지 검증
        assertEquals("중복된 이메일입니다.", thrown.getMessage());
        // 로깅
        log.info("Captured BaseException: {}", thrown.getMessage());
    }





}