package com.example.demo.src.admin;


import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.src.admin.model.AdminUserDetailRes;
import com.example.demo.src.admin.model.AdminUserRes;
import com.example.demo.src.admin.model.GetAdminUserRes;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;



    @Transactional(readOnly = true)
    public AdminUserDetailRes getAdminUserDetails(Long userId) {
        // 사용자 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND)); // 사용자를 찾을 수 없을 때 예외 처리
        // 조회된 사용자 정보를 DTO로 변환
        return new AdminUserDetailRes(user);

    }



    @Transactional(readOnly = true)
    public GetAdminUserRes getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAll(pageable);
        return  new GetAdminUserRes(userPage);

    }
}
