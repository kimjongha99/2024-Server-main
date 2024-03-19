package com.example.demo.src.comment;

import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.comment.model.CreateCommentReq;
import com.example.demo.src.comment.model.CreateCommentRes;
import com.example.demo.utils.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/comments")
public class CommentController {

    private final JwtService jwtService; // JWT 인증 서비스
    private final CommentService commentService; // 댓글 서비스


    // 댓글 생성
    @PostMapping("")
    @Operation(summary = "댓글 생성", description = "게시글에 댓글을 생성합니다.",
            responses = {
                    @ApiResponse(description = "성공", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = BaseResponse.class))),
                    @ApiResponse(description = "실패", responseCode = "400")
            })
    public BaseResponse<CreateCommentRes> createComment(@RequestBody CreateCommentReq createCommentReq) {
        Long jwtUserId = jwtService.getUserId();
        CreateCommentRes result = commentService.createComment(createCommentReq, jwtUserId);
        return new BaseResponse<>(result);
    }

//
//    // 댓글 수정
//    @PatchMapping("/{commentId}")
//    @Operation(summary = "댓글 수정", description = "특정 댓글을 수정합니다.",
//            responses = {
//                    @ApiResponse(description = "성공", responseCode = "200",
//                            content = @Content(schema = @Schema(implementation = BaseResponse.class))),
//                    @ApiResponse(description = "실패", responseCode = "400")
//            })
//    public BaseResponse<String> updateComment(@PathVariable Long commentId, @RequestBody UpdateCommentReq updateCommentReq) {
//        Long jwtUserId = jwtService.getUserId();
//        commentService.updateComment(commentId, updateCommentReq, jwtUserId);
//        return new BaseResponse<>("댓글이 수정되었습니다.");
//    }
//
//    // 댓글 삭제
//    @DeleteMapping("/{commentId}")
//    @Operation(summary = "댓글 삭제", description = "특정 댓글을 삭제합니다.",
//            responses = {
//                    @ApiResponse(description = "성공", responseCode = "200",
//                            content = @Content(schema = @Schema(implementation = BaseResponse.class))),
//                    @ApiResponse(description = "실패", responseCode = "400")
//            })
//    public BaseResponse<String> deleteComment(@PathVariable Long commentId) {
//        Long jwtUserId = jwtService.getUserId();
//        commentService.deleteComment(commentId, jwtUserService.getUserId());
//        return new BaseResponse<>("댓글이 삭제되었습니다.");
//    }
//
//    // 특정 게시글의 댓글 목록 조회
//    @GetMapping("/article/{articleId}")
//    @Operation(summary = "게시글의 댓글 목록 조회", description = "특정 게시글에 대한 모든 댓글을 조회합니다.",
//            responses = {
//                    @ApiResponse(description = "성공", responseCode = "200",
//                            content = @Content(schema = @Schema(implementation = BaseResponse.class))),
//                    @ApiResponse(description = "실패", responseCode = "400")
//            })
//    public BaseResponse<List<GetCommentRes>> getCommentsByArticle(@PathVariable Long articleId) {
//        List<GetCommentRes> comments = commentService.getCommentsByArticle(articleId);
//        return new BaseResponse<>(comments);
//    }
//
//
//
//
//





}
