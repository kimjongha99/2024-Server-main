package com.example.demo.src.admin;


import com.example.demo.common.enums.ArticleStatus;
import com.example.demo.common.enums.UserRoleEnum;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.src.admin.model.*;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.ValidationUtils;
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
     * 관리자용 최근 로그인유저  목록 조회 API
     * [GET] /app/admin/users
     * 페이지네이션을 사용하여 회원 목록을 조회합니다.
     * @param page, @param size 페이징 정보 (페이지 번호, 페이지 크기)
     * @return BaseResponse<PaginatedUserRes>
     */
    @Operation(summary = "관리자용 사용자 최근 로그인유저  목록 조회", description = "관리자가 전체 사용자 최근 로그인유저 목록을 페이지 단위로 조회합니다.", responses = {
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

        GetAdminUserRes res = adminService.getRecentLoggedInUsers(page,size);
        return new BaseResponse<>(res);
    }


     /**
      * 관리자용 신고 목록 조회 API
      * [GET] /app/admin/reports
      * 이 API는 관리자가 신고된 게시물의 목록을 조회할 수 있도록 합니다.
      * 신고된 각 게시물에 대한 정보와 함께, 신고한 사용자, 신고 이유, 신고 처리 상태 등의 세부 정보가 포함됩니다.
      * 결과는 신고 처리 상태, 신고된 날짜 등의 기준에 따라 필터링하거나 정렬할 수 있습니다.
      * 페이지네이션을 통해 결과를 페이지 단위로 조회할 수 있으며, 각 페이지의 크기는 조정할 수 있습니다.
      */
     @Operation(summary = "관리자용 신고 목록 조회", description = "관리자가 신고된 게시물의 목록을 조회합니다. 결과는 페이지 단위로 제공됩니다.", responses = {
             @ApiResponse(description = "성공", responseCode = "200", content = @Content(schema = @Schema(implementation = ReportRes.class))),
             @ApiResponse(description = "권한 없음", responseCode = "403", content = @Content) // 관리자 권한이 없는 경우
     })
     @GetMapping("/reports")
     public BaseResponse<GetAdminReportRes> getReports(
             @RequestParam(value = "page", defaultValue = "0") int page,
             @RequestParam(value = "size", defaultValue = "10") int size,
             @RequestParam(value = "status", required = false) String status // 옵션: 필터링할 신고 처리 상태
     ) {
         if (!ValidationUtils.isReportStatusValid(status)) {
             return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR);
         }
         String userRole = jwtService.getUserRole();
         if (!UserRoleEnum.ADMIN.toString().equals(userRole)) {
             return new BaseResponse<>(BaseResponseStatus.FORBIDDEN_ACCESS);
         }
         GetAdminReportRes res = adminService.getReports(page,size,status);
         return  new BaseResponse<>(res);
     }


    /**
     * 관리자용 신고 게시물 상태 변경 API
     * [PATCH] /app/admin/reports/{reportId}/status
     * 이 API는 관리자가 특정 신고된 게시물의 처리 상태를 변경할 수 있도록 합니다.
     * 상태 변경은 신고의 검토 과정이 완료되었거나 추가 조치가 필요한 경우 사용됩니다.
     * 요청은 신고 ID와 변경하고자 하는 새로운 상태를 포함해야 합니다.
     */

    @Operation(summary = "신고 게시물 상태 변경", description = "관리자가 신고된 게시물의 처리 상태를 변경합니다.", responses = {
            @ApiResponse(description = "성공", responseCode = "200", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(description = "권한 없음", responseCode = "403"),
            @ApiResponse(description = "찾을 수 없음", responseCode = "404")
    })
    @PatchMapping("/reports/{reportId}")
    public BaseResponse<String> updateReportStatus(@PathVariable Long reportId)
      {
        String userRole = jwtService.getUserRole();
        if (!UserRoleEnum.ADMIN.toString().equals(userRole)) {
            return new BaseResponse<>(BaseResponseStatus.FORBIDDEN_ACCESS);
        }
          adminService.updateReport(reportId);

        String res= "처리가 완료되었습니다";
        return new BaseResponse<>(res);
      }


    /**
     * 관리자용 아티클 상태 변경 API
     * [PATCH] /app/admin/articles/{articleId}/status
     * 이 API는 관리자가 특정 아티클의 상태를 변경할 수 있도록 합니다. 상태 옵션에는 활성(ACTIVE), 비활성(INACTIVE), 삭제됨(DELETED)이 포함됩니다.
     * 상태 변경은 아티클의 접근성 및 가시성 관리에 필요한 조치입니다. 예를 들어, 부적절한 내용을 포함한 아티클을 비활성화하거나 삭제할 수 있습니다.
     * 요청은 아티클 ID와 변경하고자 하는 새로운 상태를 포함해야 합니다.
     */
    @Operation(summary = "아티클 상태 변경", description = "관리자가 특정 아티클의 상태를 활성, 비활성, 또는 삭제됨으로 변경합니다.",  responses = {
            @ApiResponse(description = "성공", responseCode = "200", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(description = "권한 없음", responseCode = "403"),
            @ApiResponse(description = "찾을 수 없음", responseCode = "404")
    })
    @PatchMapping("/articles/{articleId}/status")
    public BaseResponse<String> updateArticleStatus(@PathVariable Long articleId, @RequestParam("status") ArticleStatus status) {
        // 상태 업데이트 로직

        adminService.updateArticleStatus(articleId,status);

        String res = "아티클 상태 변경 성공";
        return new BaseResponse<>(res);
    }


    /**
     * 관리자용  유저 상태 변경API

     */
}
