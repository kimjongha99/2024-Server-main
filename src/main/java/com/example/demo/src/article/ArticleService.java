package com.example.demo.src.article;

import com.example.demo.common.enums.ArticleStatus;
import com.example.demo.common.enums.LikeStatus;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.src.article.entity.Article;
import com.example.demo.src.article.entity.Like;
import com.example.demo.src.article.model.*;
import com.example.demo.src.report.entity.Report;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.demo.common.enums.UserState.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;



    @Transactional
    @CacheEvict(value = "articles", allEntries = true)
    public PostArticleRes createArticle(PostArticleReq postArticleReq, Long jwtUserId) {


        // JWT에서 추출한 userId와 요청에서 받은 userId가 일치하는지 확인
        if (!postArticleReq.getUserId().equals(jwtUserId)) {
            throw new BaseException(INVALID_USER_ACCESS); // 적절한 예외 처리
        }


        User user = userRepository.findByIdAndState(postArticleReq.getUserId(), ACTIVE)
                .orElseThrow(() -> new BaseException(POST_USERS_EXISTS_EMAIL));

        Article article = postArticleReq.toEntity(user);
        articleRepository.save(article);

        return new PostArticleRes(article);

    }

    @Transactional
    @CacheEvict(value = "articles", allEntries = true)
    public String patchArticle(PatchArticleReq patchArticleReq, Long jwtUserId, Long articleId) {
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
        if (patchArticleReq.getVideos() != null && !patchArticleReq.getVideos().isEmpty()) {
            article.setVideos(patchArticleReq.getVideos());
        }

        articleRepository.save(article); // 변경 사항 저장

        return "기사가 업데이트되었습니다."; // 성공 메시지 반환



    }


    @Transactional
    public String reportArticle(Long jwtUserId, Long articleId, PostReportReq postReportReq) {
        // 게시물 조회
        Article article = articleRepository.findByIdAndStatus(articleId, ArticleStatus.ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.ARTICLE_NOT_FOUND));

        // 신고자 조회
        User reporter = userRepository.findByIdAndState(jwtUserId, ACTIVE)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        // 게시물의 신고 횟수 증가
        article.setReportCount();

        // 신고 횟수가 10 이상인 경우, 게시물 상태를 비활성화로 변경
        if (article.getReportCount() >= 10) {
            article.setStatus(ArticleStatus.INACTIVE); // ArticleStatus.INACTIVE 가정
        }

        // 신고 내역 저장
        Report report = Report.builder()
                .article(article)
                .reporter(reporter)
                .reason(postReportReq.getReason())
                .build();

        article.getReports().add(report);
        articleRepository.save(article);

        return "게시물이 성공적으로 신고되었습니다.";


    }


    /**
     * 페이징 처리하여 게시글 목록 조회
     * @param page 페이지 번호 (0부터 시작)
     * @param size 페이지 당 게시글 수
     * @return 게시글 목록 , 페이지수 등
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "articles", key = "'ArticlesPage:' + #page + 'Size:' + #size")
    public ArticlePageResponse findAllBySearch(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Article> articlePage = articleRepository.findAllByStatus(ArticleStatus.ACTIVE, pageable);

        List<GetArticlePreviewRes> articles = articlePage.getContent().stream()
                .map(article -> new GetArticlePreviewRes(
                        article.getId(),
                        article.getContent(),
                        article.getContent().substring(0, Math.min(article.getContent().length(), 100)), // 내용 미리보기
                        article.getAuthor().getName()
                ))
                .collect(Collectors.toList());
        return new ArticlePageResponse(
                articles,
                articlePage.getNumber(),
                (int) articlePage.getTotalElements(),
                articlePage.getTotalPages()
        );
    }

    @Transactional
    @CacheEvict(value = "articles", allEntries = true)
    public void deleteArticleSoftly(Long jwtUserId, Long articleId) {
        // findByIdAndStatus 메서드를 사용하여 ACTIVE 상태의 게시글만 조회
        Article article = articleRepository.findByIdAndStatus(articleId, ArticleStatus.ACTIVE)
                .orElseThrow(() -> new BaseException(ARTICLE_NOT_FOUND_OR_INACTIVE)); // 게시글이 존재하지 않거나 활성 상태가 아닌 경우 예외 처리

        // 요청을 보낸 사용자가 게시글의 작성자와 일치하는지 확인
        if (!article.getAuthor().getId().equals(jwtUserId)) {
            throw new BaseException(INVALID_USER_JWT); // 적절한 예외 코드로 교체해주세요.
        }

        // 게시글 상태를 INACTIVE로 변경
        article.setStatus(ArticleStatus.INACTIVE);
        // 변경된 상태 저장
        articleRepository.save(article);



    }
    @Transactional
    public void addLikeOrCancel(Long jwtUserId, Long articleId) {

        // 인증된 사용자 확인
        User user = userRepository.findById(jwtUserId)
                .orElseThrow(() -> new BaseException(USER_NOT_FOUND)); // 적절한 예외 처리
        // 게시글 존재 여부 확인
        Article article = articleRepository.findByIdAndStatus(articleId, ArticleStatus.ACTIVE)
                .orElseThrow(() -> new BaseException(ARTICLE_NOT_FOUND_OR_INACTIVE)); // 적절한 예외 처리

        // 이미 좋아요를 눌렀는지 확인
        Optional<Like> existingLike = likeRepository.findByUserAndArticle(user, article);
        if (existingLike.isPresent()) {
            // 이미 좋아요를 눌렀다면, 상태를 확인하고 필요하다면 상태 변경
            Like like = existingLike.get();
            if (like.getLikeStatus() == LikeStatus.GOOD) {
                like.cancel(); // 상태를 CANCEL 상태로 변경
                article.decreaseFavoriteCount(); // 좋아요 감소

            } else {
                like.add(); // 상태를 LIKE 상태로 변경
                article.increaseFavoriteCount(); // 좋아요 증가

            }
            likeRepository.save(like); // 상태 변경 후 저장
        } else {
            // 좋아요 추가
            Like newLike = new Like(user, article);
            likeRepository.save(newLike);
            article.increaseFavoriteCount(); // 좋아요 증가
        }
        articleRepository.save(article); // Article의 변경 사항 저장
    }

    @Transactional(readOnly = true)
    public GetArticleRes getArticleById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new BaseException(ARTICLE_NOT_FOUND)); // 게시글을 찾을 수 없는 경우 예외 처리

        // Article 엔티티를 GetArticleRes DTO로 변환합니다.
        GetArticleRes articleRes = new GetArticleRes(
                article.getId(),
                article.getContent(),
                article.getStatus(),
                article.getReportCount(),
                article.getFavoriteCount(),
                article.getAuthor().getName(),
                article.getImages(),
                article.getVideos()
        );
        return articleRes;
    }

    public List<Long> getAllArticleIds() {
        return articleRepository.findAllArticleIds();
    }
}
