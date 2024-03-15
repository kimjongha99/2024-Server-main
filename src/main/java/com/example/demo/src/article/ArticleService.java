package com.example.demo.src.article;

import com.example.demo.common.enums.ArticleStatus;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.article.entity.Article;
import com.example.demo.src.article.model.PatchArticleReq;
import com.example.demo.src.article.model.PostArticleReq;
import com.example.demo.src.article.model.PostArticleRes;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.example.demo.common.enums.UserState.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;


    public PostArticleRes createArticle(PostArticleReq postArticleReq, Long jwtUserId) {
        try {

            // JWT에서 추출한 userId와 요청에서 받은 userId가 일치하는지 확인
            if (!postArticleReq.getUserId().equals(jwtUserId)) {
                throw new BaseException(INVALID_USER_ACCESS); // 적절한 예외 처리
            }


            User user = userRepository.findByIdAndState(postArticleReq.getUserId(), ACTIVE)
                    .orElseThrow(() -> new BaseException(POST_USERS_EXISTS_EMAIL));

            Article article = postArticleReq.toEntity(user);
            articleRepository.save(article);

            return new PostArticleRes(article);
        } catch (
                DataAccessException e) {
            throw new BaseException(DATABASE_ERROR); // 데이터베이스 접근 예외 처리


        }
    }
    @Transactional
    public String patchArticle(PatchArticleReq patchArticleReq, Long jwtUserId, Long articleId) {
        try {
            // JWT에서 추출한 userId와 요청에서 받은 userId가 일치하는지 확인
            if (!patchArticleReq.getUserId().equals(jwtUserId)) {
                throw new BaseException(INVALID_USER_ACCESS); // 적절한 예외 처리
            }


            // findByIdAndStatus 메서드를 사용하여 ACTIVE 상태의 게시글만 조회
            Article article = articleRepository.findByIdAndStatus(articleId, ArticleStatus.ACTIVE)
                    .orElseThrow(() -> new BaseException(ARTICLE_NOT_FOUND_OR_INACTIVE)); // 게시글이 존재하지 않거나 활성 상태가 아닌 경우 예외 처리

            // 변경할 내용이 있는 경우, Article 업데이트
            if (patchArticleReq.getContent() != null && !patchArticleReq.getContent().isEmpty()) {
                article.setContent(patchArticleReq.getContent());
            }
            if (patchArticleReq.getImages() != null && !patchArticleReq.getImages().isEmpty()) {
                article.setImages(patchArticleReq.getImages());
            }

            articleRepository.save(article); // 변경 사항 저장

            return "기사가 업데이트되었습니다."; // 성공 메시지 반환


        } catch (
                DataAccessException e) {
            throw new BaseException(DATABASE_ERROR); // 데이터베이스 접근 예외 처리

        }

    }

}