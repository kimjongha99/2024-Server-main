package com.example.demo.src.admin;


import com.example.demo.common.enums.UserRoleEnum;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.src.admin.model.AdminUserDetailRes;
import com.example.demo.src.admin.model.AdminUserRes;
import com.example.demo.src.admin.model.GetAdminUserRes;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.utils.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/admin")
public class AdminController {

    private final  AdminService adminService;
    private final JwtService jwtService;


    /**
     * 관리자용 회원 1명 조회 API
     * [GET] /app/admin/users/:userId
     * @return BaseResponse<GetUserRes>
     */
    @Operation(summary = "관리자용 사용자 정보 조회", description = "관리자가 사용자 ID로 단일 사용자의 상세 정보를 검색합니다.", responses = {
            @ApiResponse(description = "성공", responseCode = "200", content = @Content(schema = @Schema(implementation = GetUserRes.class))),
            @ApiResponse(description = "사용자를 찾을 수 없습니다", responseCode = "404"),
            @ApiResponse(description = "권한 없음", responseCode = "403") // 관리자 권한이 없는 경우
    })
    @GetMapping("/users/{userId}")
    public BaseResponse<AdminUserDetailRes> getAdminUserDetails(@PathVariable Long userId) {
        String userRole = jwtService.getUserRole();
        if (!UserRoleEnum.ADMIN.toString().equals(userRole)) {
            return new BaseResponse<>(BaseResponseStatus.FORBIDDEN_ACCESS);
        }

        AdminUserDetailRes res =  adminService.getAdminUserDetails(userId);
        return new BaseResponse<>(res);

    }

    /**
     * 관리자용 회원 목록 조회 API
     * [GET] /app/admin/users
     * 페이지네이션을 사용하여 회원 목록을 조회합니다.
     * @param page, @param size 페이징 정보 (페이지 번호, 페이지 크기)
     * @return BaseResponse<PaginatedUserRes>
     */
    @Operation(summary = "관리자용 사용자 목록 조회", description = "관리자가 전체 사용자 목록을 페이지 단위로 조회합니다.", responses = {
            @ApiResponse(description = "성공", responseCode = "200", content = @Content(schema = @Schema(implementation = AdminUserRes.class))),
            @ApiResponse(description = "권한 없음", responseCode = "403") // 관리자 권한이 없는 경우
    })
    @GetMapping("/users")
    public BaseResponse<GetAdminUserRes> getUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        String userRole = jwtService.getUserRole();
        if (!UserRoleEnum.ADMIN.toString().equals(userRole)) {
            return new BaseResponse<>(BaseResponseStatus.FORBIDDEN_ACCESS);
        }

        GetAdminUserRes res = adminService.getUsers(page,size);
        return new BaseResponse<>(res);
    }


}
