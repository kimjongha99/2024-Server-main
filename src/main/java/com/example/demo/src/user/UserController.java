package com.example.demo.src.user;


import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.oauth.infra.kakao.KakaoLoginParams;
import com.example.demo.common.oauth.application.OAuthLoginService;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.ValidationRegex;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.user.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


import static com.example.demo.common.response.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/users")
public class UserController {


    private final UserService userService;

    private final OAuthLoginService oAuthLoginService;
    private final JwtService jwtService;


    /**
     * 회원가입 API
     * [POST] /app/users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @Operation(summary = "새 사용자 만들기", description = "제공된 정보로 새로운 사용자를 등록합니다..", responses = {
            @ApiResponse(description = "성공", responseCode = "200", content = @Content(schema = @Schema(implementation = PostUserRes.class))),
            @ApiResponse(description = "잘못된 이메일 형식", responseCode = "400"),
            @ApiResponse(description = "이메일이 이미 존재합니다", responseCode = "409")
    })
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        // 이메일이 null인지 체크
        if(postUserReq.getEmail() == null){
            return new BaseResponse<>(USERS_EMPTY_EMAIL); // 적절한 에러 코드 상수를 사용하세요.
        }

        // 이메일 유효성 검사
        if(!ValidationRegex.isRegexEmail(postUserReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL); // 적절한 에러 코드 상수를 사용하세요.
        }

        // 비밀번호 길이 검사
        if(!ValidationRegex.isPasswordValid(postUserReq.getPassword())){
            return new BaseResponse<>(POST_USERS_INVALID_PASSWORD); // 적절한 에러 코드 상수를 정의하고 사용하세요.
        }

        // 이름 유효성 검사
        if(!ValidationRegex.isNameValid(postUserReq.getName())){
            return new BaseResponse<>(POST_USERS_INVALID_NAME); // 적절한 에러 코드 상수를 정의하고 사용하세요.
        }

        // 생일 날짜 유효성 검사
        if(!ValidationRegex.isBirthDateValid(postUserReq.getBirthDate().toString())){
            return new BaseResponse<>(POST_USERS_INVALID_BIRTHDATE); // 적절한 에러 코드 상수를 정의하고 사용하세요.
        }
        PostUserRes postUserRes = userService.createUser(postUserReq);
        return new BaseResponse<>(postUserRes);
    }

    /**
     * 회원 조회 API
     * [GET] /users
     * 회원 번호 및 이메일 검색 조회 API
     * [GET] /app/users? Email=
     * @return BaseResponse<List<GetUserRes>>
     */
    //Query String
    @Operation(summary = "사용자 조회", description = "사용자 목록을 검색합니다. 제공된 경우 이메일로 필터링 가능.", responses = {
            @ApiResponse(description = "성공적인 운영", responseCode = "200", content = @Content(schema = @Schema(implementation = GetUserRes.class)))
    })
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/users
    public BaseResponse<List<GetUserRes>> getUsers(@RequestParam(required = false) String Email) {
        if(Email == null){
            List<GetUserRes> getUsersRes = userService.getUsers();
            return new BaseResponse<>(getUsersRes);
        }
        // Get Users
        List<GetUserRes> getUsersRes = userService.getUsersByEmail(Email);
        return new BaseResponse<>(getUsersRes);
    }

    /**
     * 회원 1명 조회 API
     * [GET] /app/users/:userId
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
    @Operation(summary = "ID로 사용자 가져오기", description = "사용자 ID로 단일 사용자를 검색합니다..", responses = {
            @ApiResponse(description = "성공적인 운영", responseCode = "200", content = @Content(schema = @Schema(implementation = GetUserRes.class))),
            @ApiResponse(description = "사용자를 찾을 수 없습니다", responseCode = "404")
    })
    @ResponseBody
    @GetMapping("/{userId}") // (GET) 127.0.0.1:9000/app/users/:userId
    public BaseResponse<GetUserRes> getUser(@PathVariable("userId") Long userId) {
        GetUserRes getUserRes = userService.getUser(userId);
        return new BaseResponse<>(getUserRes);
    }



    /**
     * 유저정보변경 API
     * [PATCH] /app/users/:userId
     * @return BaseResponse<String>
     */
    @Operation(summary = "사용자 정보 수정", description = "userId로 식별된 사용자의 정보을 업데이트합니다..", responses = {
            @ApiResponse(description = "Successful operation", responseCode = "200", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(description = "User not found", responseCode = "404"),
            @ApiResponse(description = "Unauthorized", responseCode = "401")
    })
    @ResponseBody
    @PatchMapping("/{userId}")
    public BaseResponse<String> modifyUserName(@PathVariable("userId") Long userId, @RequestBody PatchUserReq patchUserReq){

        Long jwtUserId = jwtService.getUserId();

        userService.modifyUserName(userId, patchUserReq);

        String result = "수정 완료!!";
        return new BaseResponse<>(result);

    }

    /**
     * 유저정보 소프트  삭제 API
     * [DELETE] /app/users/:userId
     * @return BaseResponse<String>
     */
    @Operation(summary = "사용자 소프트  삭제", description = "userId로 식별된 사용자를 소프트 제거합니다..", responses = {
            @ApiResponse(description = "Successful operation", responseCode = "200", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(description = "User not found", responseCode = "404"),
            @ApiResponse(description = "Unauthorized", responseCode = "401")
    })
    @ResponseBody
    @PatchMapping("/{userId}/delete")
    public BaseResponse<String> deleteUser(@PathVariable("userId") Long userId){
        Long jwtUserId = jwtService.getUserId();

        userService.deleteUser(userId);

        String result = "삭제 완료!!";
        return new BaseResponse<>(result);
    }

    /**
     * 로그인 API
     * [POST] /app/users/logIn
     * @return BaseResponse<PostLoginRes>
     */
    @Operation(summary = "로그인", description = "사용자를 인증하고 액세스 토큰을 반환합니다..", responses = {
            @ApiResponse(description = "Successful operation", responseCode = "200", content = @Content(schema = @Schema(implementation = PostLoginRes.class))),
            @ApiResponse(description = "Invalid credentials", responseCode = "401")
    })
    @ResponseBody
    @PostMapping("/logIn")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){
        PostLoginRes postLoginRes = userService.logIn(postLoginReq);
        return new BaseResponse<>(postLoginRes);
    }



    /**
     * 카카오 로그인 API
     * [POST] /app/users/kakao
     * @param params 카카오 로그인 요청 파라미터
     * @return BaseResponse<AuthTokens>
     */
    @Operation(summary = "카카오 로그인", description = "카카오 계정으로 로그인하고 토큰을 반환합니다.", responses = {
            @ApiResponse(description = "성공", responseCode = "200", content = @Content(schema = @Schema(implementation = GetSocialOAuthRes.class))),
            @ApiResponse(description = "잘못된 요청", responseCode = "400"),
            @ApiResponse(description = "인증 실패", responseCode = "401")
    })
    @PostMapping("/kakao")
    public BaseResponse<GetSocialOAuthRes> loginKakao(@RequestBody KakaoLoginParams params) {
        GetSocialOAuthRes tokens = oAuthLoginService.login(params);
        return new BaseResponse<>(tokens);
    }





    /**
     * 소셜로그인시 추가 정보 요청 API
     * [POST] /app/users/login-add/:userId
     *
     * @return BaseResponse<String>
     */
    @Operation(summary = "소셜로그인 추가 정보입력",
            description = "소셜로그인 때 추가정보가 없는 경우 추가정보 입력",
            responses = {
                    @ApiResponse(description = "성공", responseCode = "200", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(description = "사용자를 찾을 수 없습니다", responseCode = "404"),
                    @ApiResponse(description = "잘못된 요청", responseCode = "400")
            })
    @PostMapping("/login-add/{userId}")
    public BaseResponse<String> addSocialLoginAdditionalInfo(@PathVariable Long userId, @RequestBody AdditionalInfo additionalInfo) {
        // 토큰에서 userId 추출
        Long tokenUserId = jwtService.getUserId();

        // PathVariable의 userId와 토큰에서 추출한 userId 비교
        if (!userId.equals(tokenUserId)) {
            return new BaseResponse<>(NOT_FIND_USER);
        }
        userService.addAdditionalInfo(userId, additionalInfo);
        String result = "소셜 추가정보 입력 완료!!";
        return new BaseResponse<>(result);


    }



}
