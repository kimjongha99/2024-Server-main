package com.example.demo.src.user;



import com.example.demo.common.enums.UserRoleEnum;
import com.example.demo.common.enums.UserState;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.user.entity.User;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.demo.common.enums.UserState.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Transactional
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Transactional
    //POST
    public PostUserRes createUser(PostUserReq postUserReq) {
        // 개인정보 처리방침에 대한 동의 여부 확인
        if (!postUserReq.isPrivacyPolicyAgreed()) {
            throw new BaseException(PRIVACY_POLICY_AGREEMENT_REQUIRED);
        }

        // 위치기반 서비스에 대한 동의 여부 확인
        if (!postUserReq.isLocationBasedServicesAgreed()) {
            throw new BaseException(LOCATION_BASED_SERVICES_AGREEMENT_REQUIRED);
        }

        // 데이터 정책에 대한 동의 여부 확인
        if (!postUserReq.isDataPolicyAgreed()) {
            throw new BaseException(DATA_POLICY_AGREEMENT_REQUIRED);
        }

        //중복 체크
        Optional<User> checkUser = userRepository.findByEmailAndState(postUserReq.getEmail(), ACTIVE);
        if(checkUser.isPresent() == true){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }

        String encryptPwd;
        try {
            encryptPwd = new SHA256().encrypt(postUserReq.getPassword());
            postUserReq.setPassword(encryptPwd);
        } catch (Exception exception) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        User saveUser = userRepository.save(postUserReq.toEntity());
        return new PostUserRes(saveUser.getId());

    }
    @Transactional

    public void modifyUserName(Long userId, PatchUserReq patchUserReq) {
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        user.updateName(patchUserReq.getName());
    }

    @Transactional
    public void deleteUser(Long userId) {
        // 유저를 찾되, 상태가 ACTIVE인 유저만 찾습니다.
        User user = userRepository.findByIdAndState(userId, UserState.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER)); // 유저를 찾지 못했다면 예외를 던집니다.

        // 유저의 상태를 DEACTIVATED로 변경합니다.
        user.setStatus(UserState.DEACTIVATED);

        // 변경된 상태를 저장합니다.
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<GetUserRes> getUsers() {
        List<GetUserRes> getUserResList = userRepository.findAllByState(ACTIVE).stream()
                .map(GetUserRes::new)
                .collect(Collectors.toList());
        return getUserResList;
    }

    @Transactional(readOnly = true)
    public List<GetUserRes> getUsersByEmail(String email) {
        List<GetUserRes> getUserResList = userRepository.findAllByEmailAndState(email, ACTIVE).stream()
                .map(GetUserRes::new)
                .collect(Collectors.toList());
        return getUserResList;
    }


    @Transactional(readOnly = true)
    public GetUserRes getUser(Long userId) {
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        return new GetUserRes(user);
    }

    @Transactional(readOnly = true)
    public boolean checkUserByEmail(String email) {
        Optional<User> result = userRepository.findByEmailAndState(email, ACTIVE);
        if (result.isPresent()) return true;
        return false;
    }

    public PostLoginRes logIn(PostLoginReq postLoginReq) {
        User user = userRepository.findByEmailAndState(postLoginReq.getEmail(), ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));

        String encryptPwd;
        try {
            encryptPwd = new SHA256().encrypt(postLoginReq.getPassword());
        } catch (Exception exception) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        if(user.getPassword().equals(encryptPwd)){
            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user); // 업데이트된 정보를 저장


            Long userId = user.getId();
            UserRoleEnum role = user.getRole();
            String jwt = jwtService.createJwt(userId,role);
            return new PostLoginRes(userId,jwt);
        } else{
            throw new BaseException(FAILED_TO_LOGIN);
        }

    }

    public GetUserRes getUserByEmail(String email) {
        User user = userRepository.findByEmailAndState(email, ACTIVE).orElseThrow(() -> new BaseException(NOT_FIND_USER));
        return new GetUserRes(user);
    }

    public void addAdditionalInfo(Long userId, AdditionalInfo additionalInfo) {
        // 1. 사용자 조회
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER)); // 사용자를 찾을 수 없을 때 예외 처리

        user.setBirthDate(additionalInfo.getBirthDate());
        user.setPrivacyPolicyAgreed(additionalInfo.isPrivacyPolicyAgreed());
        user.setDataPolicyAgreed(additionalInfo.isDataPolicyAgreed());
        user.setLocationBasedServicesAgreed(additionalInfo.isLocationBasedServicesAgreed());
        user.setLastAgreedAt(LocalDateTime.now()); // 현재 시각을 마지막 동의 시각으로 설정
        // 3. 업데이트된 사용자 정보 저장
        userRepository.save(user);

    }
}
