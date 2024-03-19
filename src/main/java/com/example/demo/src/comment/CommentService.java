package com.example.demo.src.comment;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.article.ArticleRepository;
import com.example.demo.src.article.entity.Article;
import com.example.demo.src.comment.entity.Comment;
import com.example.demo.src.comment.model.CreateCommentReq;
import com.example.demo.src.comment.model.CreateCommentRes;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.common.response.BaseResponseStatus.ARTICLE_NOT_FOUND;
import static com.example.demo.common.response.BaseResponseStatus.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreateCommentRes createComment(CreateCommentReq createCommentReq, Long userId) {
        // 사용자 검증
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(USER_NOT_FOUND));

        // 게시글 검증
        Article article = articleRepository.findById(createCommentReq.getArticleId())
                .orElseThrow(() -> new BaseException(ARTICLE_NOT_FOUND));
        // DTO에서 엔티티로 변환
        Comment comment = createCommentReq.toEntity(article, user);

        // 댓글 저장
        comment = commentRepository.save(comment);

        // 생성된 댓글 정보를 기반으로 응답 객체 생성
        return new CreateCommentRes(comment);


    }
}
