package com.example.demo.src.comment;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.src.article.ArticleRepository;
import com.example.demo.src.article.entity.Article;
import com.example.demo.src.comment.entity.Comment;
import com.example.demo.src.comment.model.*;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.demo.common.response.BaseResponseStatus.*;

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

    @Transactional
    public void updateComment(Long commentId, UpdateCommentReq updateCommentReq, Long jwtUserId) {

        // 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(COMMENT_NOT_FOUND));

        // 댓글 작성자와 요청 사용자 ID 비교
        if (!comment.getUser().getId().equals(jwtUserId)) {
            throw new BaseException(INVALID_USER_ACCESS); // 적절한 예외 처리
        }
        comment.setContent(updateCommentReq.getContent());
        commentRepository.save(comment);

    }

    @Transactional
    public void deleteComment(Long commentId, Long jwtUserId) {
        // 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(COMMENT_NOT_FOUND));

        // 댓글 작성자와 요청 사용자 ID 비교
        if (!comment.getUser().getId().equals(jwtUserId)) {
            throw new BaseException(INVALID_USER_ACCESS); // 적절한 예외 처리
        }

        // 댓글 삭제
        commentRepository.deleteById(commentId);
    }

    @Transactional(readOnly = true)
    public GetCommentRes getCommentsByArticle(Long articleId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentsPage = commentRepository.findByArticleId(articleId, pageable);

        List<CommentInfo> commentInfos = commentsPage.getContent().stream()
                .map(comment -> new CommentInfo(
                        comment.getId(),
                        comment.getContent(),
                        comment.getUser().getId(),
                        comment.getUser().getName()))
                .collect(Collectors.toList());

        return new GetCommentRes(
                commentInfos,
                commentInfos.size(),
                commentsPage.getTotalPages(),
                commentsPage.getTotalElements(),
                commentsPage.isFirst(),
                commentsPage.isLast());
    }

}
