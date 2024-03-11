package com.example.demo.src.user;


import com.example.demo.common.Constant.SocialLoginType;
import com.example.demo.common.oauth.OAuthService;
import com.example.demo.utils.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.user.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


import static com.example.demo.common.response.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/users")
public class UserController {


    private final UserService userService;

    private final OAuthService oAuthService;

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
        if(postUserReq.getEmail() == null){
            return new BaseResponse<>(USERS_EMPTY_EMAIL);
        }
        //이메일 정규표현
        if(!isRegexEmail(postUserReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
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
     * 유저정보삭제 API
     * [DELETE] /app/users/:userId
     * @return BaseResponse<String>
     */
    @Operation(summary = "사용자 삭제", description = "userId로 식별된 사용자를 시스템에서 제거합니다..", responses = {
            @ApiResponse(description = "Successful operation", responseCode = "200", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(description = "User not found", responseCode = "404"),
            @ApiResponse(description = "Unauthorized", responseCode = "401")
    })
    @ResponseBody
    @DeleteMapping("/{userId}")
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
        // TODO: 로그인 값들에 대한 형식적인 validatin 처리해주셔야합니다!
        // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.
        PostLoginRes postLoginRes = userService.logIn(postLoginReq);
        return new BaseResponse<>(postLoginRes);
    }


    /**
     * 유저 소셜 가입, 로그인 인증으로 리다이렉트 해주는 url
     * [GET] /app/users/auth/:socialLoginType/login
     * @return void
     */
    @Operation(summary = "소셜 로그인 리디렉션", description = "지정된 소셜 로그인 유형에 대한 소셜 로그인 페이지로 리디렉션됩니다.", responses = {
            @ApiResponse(description = "Redirection successful", responseCode = "302"),
            @ApiResponse(description = "Invalid social login type", responseCode = "400")
    })
    @GetMapping("/auth/{socialLoginType}/login")
    public void socialLoginRedirect(@PathVariable(name="socialLoginType") String SocialLoginPath) throws IOException {
        SocialLoginType socialLoginType= SocialLoginType.valueOf(SocialLoginPath.toUpperCase());
        oAuthService.accessRequest(socialLoginType);
    }


    /**
     * Social Login API Server 요청에 의한 callback 을 처리
     * @param socialLoginPath (GOOGLE, FACEBOOK, NAVER, KAKAO)
     * @param code API Server 로부터 넘어오는 code
     * @return SNS Login 요청 결과로 받은 Json 형태의 java 객체 (access_token, jwt_token, user_num 등)
     */
    @Operation(summary = "소셜 로그인 콜백 처리", description = "소셜 로그인 공급자의 콜백을 처리하고 사용자 토큰에 대한 코드를 교환합니다..", responses = {
            @ApiResponse(description = "Successful operation", responseCode = "200", content = @Content(schema = @Schema(implementation = GetSocialOAuthRes.class))),
            @ApiResponse(description = "Error during callback processing", responseCode = "500")
    })
    @ResponseBody
    @GetMapping(value = "/auth/{socialLoginType}/login/callback")
    public BaseResponse<GetSocialOAuthRes> socialLoginCallback(
            @PathVariable(name = "socialLoginType") String socialLoginPath,
            @RequestParam(name = "code") String code) throws IOException, BaseException{
        log.info(">> 소셜 로그인 API 서버로부터 받은 code : {}", code);
        SocialLoginType socialLoginType = SocialLoginType.valueOf(socialLoginPath.toUpperCase());
        GetSocialOAuthRes getSocialOAuthRes = oAuthService.oAuthLoginOrJoin(socialLoginType,code);
        return new BaseResponse<>(getSocialOAuthRes);
    }


}
