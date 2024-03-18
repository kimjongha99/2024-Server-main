package com.example.demo.src.article;

import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.article.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.ValidationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
     * 게시글 목록 조회 API (무한 스크롤 지원)
     * [GET] /app/articles?page=&size=
     * @param page 페이지 번호 (0부터 시작)
     * @param size 페이지 당 게시글 수
     * @return BaseResponse<List<GetArticlePreviewRes>>
     */
    @GetMapping("/articles")
    public BaseResponse<List<GetArticlePreviewRes>> findPostByPaging(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            List<GetArticlePreviewRes> articlePreviews = articleService.findAllBySearch(page, size);
            return new BaseResponse<>(articlePreviews);
        } catch (Exception e) {
            return new BaseResponse<>(UNEXPECTED_ERROR); // Make sure to define this error code appropriately
        }
    }

}
