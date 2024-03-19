package com.example.demo.src.article;

import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.article.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.ValidationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.common.response.BaseResponseStatus.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/articles")
public class ArticleController {




    private final ArticleService articleService;
    private final JwtService jwtService;


    /**
     * 게시글 생성 API
     * [POST] /app/articles
     * @return BaseResponse<PostArticlesRes>
     */
    @Operation(summary = "새 게시글 만들기", description = "제공된 정보로 새로운 게시물을 등록합니다..", responses = {
            @ApiResponse(description = "성공", responseCode = "200", content = @Content(schema = @Schema(implementation = PostArticleRes.class))),
            @ApiResponse(description = "실패", responseCode = "400"),
    })
    @PostMapping("")
    public BaseResponse<PostArticleRes> createArticle(@RequestBody PostArticleReq postArticleReq) {
        try {

            if (!ValidationUtils.isContentLengthValid(postArticleReq.getContent())) {
            return new BaseResponse<>(POST_ARTICLES_INVALID_CONTENT); // 적절한 에러 코드 상수 사용
        }

        if (!ValidationUtils.isImageCountValid(postArticleReq.getImages())) {
            return new BaseResponse<>(POST_ARTICLES_EXCEEDS_IMAGE_LIMIT); // 적절한 에러 코드 상수 사용
        }

        // 토큰에서 사용자 ID 추출
        Long jwtUserId = jwtService.getUserId();

        PostArticleRes postArticleRes = articleService.createArticle(postArticleReq, jwtUserId);
        return  new BaseResponse<>(postArticleRes);
        } catch (Exception e) {
            // 예상치 못한 예외 처리
            return new BaseResponse<>(UNEXPECTED_ERROR);
        }
    }


    /**
     * 게시글 수정 API
     * [Patch] /app/articles/{id}
     * @return BaseResponse<String>
     */
    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.", responses = {
            @ApiResponse(description = "성공", responseCode = "200", content = @Content(schema = @Schema(implementation = PatchArticleReq.class))),
            @ApiResponse(description = "실패", responseCode = "400"),
    })
    @PatchMapping("/{id}")
    public BaseResponse<String> updateArticle(
            @RequestBody PatchArticleReq patchArticleReq,
            @PathVariable Long id)
    {
        try {
            // 토큰에서 사용자 ID 추출
            Long jwtUserId = jwtService.getUserId();
            String result = articleService.patchArticle(patchArticleReq,jwtUserId,id);
            return  new BaseResponse<>(result);
        }catch (Exception e) {
            // 예상치 못한 예외 처리
            return new BaseResponse<>(UNEXPECTED_ERROR);
        }
    }

    /**
     * 게시글 신고 API
     * [Post] /app/articles/{id}
     * @return BaseResponse<String>
     */
    @Operation(summary = "게시글 신고", description = "게시글  신고합니다.", responses = {
            @ApiResponse(description = "성공", responseCode = "200", content = @Content(schema = @Schema(implementation = PatchArticleReq.class))),
            @ApiResponse(description = "실패", responseCode = "400"),
    })
    @PostMapping("/reports/{id}")
    public BaseResponse<String> reportArticle(
            @PathVariable Long id,
            @RequestBody PostReportReq postReportReq
    ){
        try {
            // 토큰에서 사용자 ID 추출
            Long jwtUserId = jwtService.getUserId();

            String result = articleService.reportArticle(jwtUserId,id,postReportReq);
            return  new BaseResponse<>(result);
        }catch (Exception e) {
            // 예상치 못한 예외 처리
            return new BaseResponse<>(UNEXPECTED_ERROR);
        }
    }

    /**
     * 게시글 목록 조회 API
     * [GET] /app/articles?page=&size=
     * @param page 페이지 번호 (0부터 시작)
     * @param size 페이지 당 게시글 수
     * @return BaseResponse<List<GetArticlePreviewRes>>
     */
    @GetMapping("")
    public BaseResponse<List<GetArticlePreviewRes>> findPostByPaging(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            List<GetArticlePreviewRes> articlePreviews = articleService.findAllBySearch(page, size);
            return new BaseResponse<>(articlePreviews);
        } catch (Exception e) {
            return new BaseResponse<>(UNEXPECTED_ERROR);
        }
    }


    /**
     * 게시글 소프트 삭제 API
     * [DELETE] /app/articles/{id}
     * @param id 삭제할 게시글의 ID
     * @return BaseResponse<String>
     */
    @Operation(summary = "게시글 소프트 삭제",
            description = "게시글을 소프트 삭제합니다. Header에 `X-ACCESS-TOKEN`이 필요합니다. `Path Variable`로 게시글 아이디를 입력하세요. 게시글 삭제 권한을 검증 후 통과하지 못하면 삭제가 진행되지 않습니다.",
            security = @SecurityRequirement(name = "X-ACCESS-TOKEN"),
            responses = {
                    @ApiResponse(description = "성공", responseCode = "200", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(description = "실패", responseCode = "400"),
            })
    @PatchMapping("/{id}/deactivate")
    public BaseResponse<String> deleteArticleSoftly(@PathVariable Long id) {
        try {
            // 토큰에서 사용자 ID 추출 (소프트 삭제 권한 검증을 위해 사용할 수 있습니다.)
            Long jwtUserId = jwtService.getUserId();

            // 소프트 삭제 처리를 위한 서비스 메소드 호출
            articleService.deleteArticleSoftly(jwtUserId, id);
            String result= "소프트 삭제가 완료되었습니다";
            return new BaseResponse<>(result);
        } catch (Exception e) {
            // 예상치 못한 예외 처리
            return new BaseResponse<>(UNEXPECTED_ERROR);
        }
    }
    /**
     * 게시글 좋아요 추가 API
     * [POST] /app/articles/{articleId}/like
     * @param articleId 좋아요를 누를 게시글의 ID
     * @return BaseResponse<String>
     */
    @Operation(summary = "게시글 좋아요,싫어요 추가",
            description = "특정 게시글에 좋아요 , 싫어요 를 추가합니다. # Header에 `X-ACCESS-TOKEN`이 필요합니다. `Path Variable`로 좋아요를 누를 게시글의 `articleId`를 입력하세요.",
            security = @SecurityRequirement(name = "X-ACCESS-TOKEN"),
            responses = {
                    @ApiResponse(description = "성공", responseCode = "200", content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(description = "실패", responseCode = "400"),
            })
    @PostMapping("/articles/{articleId}/like")
    public BaseResponse<String> addLikeToArticle(@PathVariable Long articleId) {
            // 토큰에서 사용자 ID 추출
            Long jwtUserId = jwtService.getUserId();

            // 좋아요 추가 처리를 위한 서비스 메소드 호출
             articleService.addLikeOrCancel(jwtUserId, articleId);
            String result = "요청이 성공했습니다.";
            return new BaseResponse<>(result);

    }


}
