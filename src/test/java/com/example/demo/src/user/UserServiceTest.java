package com.example.demo.src.user;

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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

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
        // Given
        PostUserReq postUserReq = new PostUserReq("user@example.com", "password", "Test User", false, true);
        String encryptedPassword = SHA256.encrypt(postUserReq.getPassword()); // // SHA256을 직접  처리

        // 빌더 패턴을 사용하여 mockUser 객체 생성, ID 값을 포함해 설정
        User mockUser = User.builder()
                .id(1L) // mockUser에 ID 값 명시적으로 설정
                .email(postUserReq.getEmail())
                .password(encryptedPassword)
                .name(postUserReq.getName())
                .isOAuth(postUserReq.isOAuth())
                .privacyPolicyAgreed(postUserReq.isPrivacyPolicyAgreed())
                .build();

        when(userRepository.findByEmailAndState(postUserReq.getEmail(), User.State.ACTIVE)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(mockUser); // Return mockUser

        // When
        PostUserRes postUserRes = userService.createUser(postUserReq);

        // Then
        assertNotNull(postUserRes, "PostUserRes는 null이 아니어야 합니다.");
        log.info("PostUserRes is not null.");

        assertNotNull(postUserRes.getId(), "생성된 사용자 ID는 null이 아니어야 합니다.");

        assertEquals(mockUser.getId(), postUserRes.getId(), "PostUserRes 객체의 ID가 mockUser의 ID와 일치해야 합니다.");
        log.info("PostUserRes ID matches mockUser ID: {}", postUserRes.getId());


        // UserRepository의 findByEmailAndState 메소드가 정확한 인자로 호출되었는지 검증
        verify(userRepository).findByEmailAndState(postUserReq.getEmail(), User.State.ACTIVE);
        // UserRepository의 findByEmailAndState 메소드가 정확한 인자로 호출되었는지 검증
        verify(userRepository).findByEmailAndState(postUserReq.getEmail(), User.State.ACTIVE);
        // UserRepository의 save 메소드가 정확한 User 객체로 호출되었는지 검증
        verify(userRepository).save(any(User.class)); // 더 구체적인 검증을 위해 any() 대신 eq(가상User)를 사용할 수도 있습니다.

        // User 객체의 상태 검증
        assertEquals(postUserReq.getEmail(), mockUser.getEmail(), "이메일이 일치해야 합니다.");
        log.info("Email matches: {}", postUserReq.getEmail().equals(mockUser.getEmail()));

        assertEquals(encryptedPassword, mockUser.getPassword(), "비밀번호가 암호화되어 저장되어야 합니다.");
        assertEquals(postUserReq.getName(), mockUser.getName(), "이름이 일치해야 합니다.");
        assertEquals(postUserReq.isOAuth(), mockUser.isOAuth(), "OAuth 상태가 일치해야 합니다.");
        assertEquals(postUserReq.isPrivacyPolicyAgreed(), mockUser.isPrivacyPolicyAgreed(), "개인정보 처리방침 동의 상태가 일치해야 합니다.");
    }


    @Test
    @DisplayName("유저 개인정보 처리방침 동의 없는 경우")
    void createUser_PrivacyPolicyNotAgreed_ThrowsException() {
        // Given
        PostUserReq postUserReq = new PostUserReq("user@example.com", "password", "Test User", false, false);

        // When & Then
        assertThrows(BaseException.class, () -> userService.createUser(postUserReq), "개인정보 처리방침에 대한 동의가 필요합니다.");
    }

    @Test
    @DisplayName("이미 존재하는 이메일로 사용자 생성 시도")
    void createUser_ExistingEmail_ThrowsException() {
        // Given
        PostUserReq secondUserReq = new PostUserReq("user@example.com", "password", "User Two", true, true);
        User user = User.builder()
                .email("example@example.com")
                .password("password")
                .name("Test User")
                .isOAuth(false)
                .privacyPolicyAgreed(true)
                .build();
        // 이미 존재하는 사용자를 나타내기 위해 userRepository.findByEmailAndState 모킹
        when(userRepository.findByEmailAndState(secondUserReq.getEmail(), User.State.ACTIVE))
                .thenReturn(Optional.of(user)); // 수정된 부분: User 객체는 빌더를 사용하여 생성된 객체를 사용
        // When & Then
        BaseException thrown = assertThrows(BaseException.class, () -> userService.createUser(secondUserReq));
        // 예외 메시지 검증
        assertEquals("중복된 이메일입니다.", thrown.getMessage());
        // 로깅
        log.info("Captured BaseException: {}", thrown.getMessage());
    }





}