package com.example.demo.src.article;

import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.article.entity.Article;
import com.example.demo.src.article.model.PostArticleReq;
import com.example.demo.src.article.model.PostArticleRes;
import com.example.demo.src.user.model.PostUserReq;
import com.example.demo.src.user.model.PostUserRes;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.ValidationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.common.response.BaseResponseStatus.POST_ARTICLES_EXCEEDS_IMAGE_LIMIT;
import static com.example.demo.common.response.BaseResponseStatus.POST_ARTICLES_INVALID_CONTENT;

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
            @ApiResponse(description = "~~~~~~", responseCode = "400"),
            @ApiResponse(description = "~~~~~~~~~~~", responseCode = "409")
    })
    @PostMapping("")
    public BaseResponse<PostArticleRes> createArticle(@RequestBody PostArticleReq postArticleReq) {
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
    }






}
