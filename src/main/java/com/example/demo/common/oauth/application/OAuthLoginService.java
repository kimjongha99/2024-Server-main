package com.example.demo.common.oauth.application;

import com.example.demo.common.enums.UserRoleEnum;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.oauth.domain.OAuthInfoResponse;
import com.example.demo.common.oauth.domain.OAuthLoginParams;
import com.example.demo.common.oauth.domain.RequestOAuthInfoService;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import com.example.demo.src.user.model.GetSocialOAuthRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.example.demo.common.enums.UserState.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.NOT_FIND_USER;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RequestOAuthInfoService requestOAuthInfoService;


    public GetSocialOAuthRes login(OAuthLoginParams params) {
        // OAuth 제공자로부터 사용자 정보를 요청
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        // 사용자를 찾거나 새로 생성하고 사용자 ID를 반환
        Long userId = findOrCreateUser(oAuthInfoResponse);
        // 여기서 사용자의 마지막 로그인 시간을 업데이트
        updateUserLastLogin(userId);

        // 사용자 역할을 기반으로 JWT 생성
        String jwtToken = jwtService.createJwt(userId, UserRoleEnum.USER);

        String tokenType = "Bearer";

        // GetSocialOAuthRes 객체를 생성하고 반환
        return new GetSocialOAuthRes(userId,jwtToken, tokenType);
    }

    private void updateUserLastLogin(Long userId) {
        // UserRepository를 사용하여 사용자 엔티티를 조회
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));

        // 마지막 로그인 시간 설정
        user.setLastLoginAt(LocalDateTime.now());

        // 업데이트된 정보를 저장
        userRepository.save(user);
    }

    private Long findOrCreateUser(OAuthInfoResponse oAuthInfoResponse) {
        return userRepository.findByEmail(oAuthInfoResponse.getEmail())
                .map(User::getId)
                .orElseGet(() -> newUser(oAuthInfoResponse));
    }

    private Long newUser(OAuthInfoResponse oAuthInfoResponse) {
        User oauthUser = new User(
                oAuthInfoResponse.getEmail(),
                oAuthInfoResponse.getNickname(),
                oAuthInfoResponse.getOAuthProvider()
        );

        return userRepository.save(oauthUser).getId();
    }
}