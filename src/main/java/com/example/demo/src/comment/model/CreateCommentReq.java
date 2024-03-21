package com.example.demo.src.comment.model;


import com.example.demo.src.article.entity.Article;
import com.example.demo.src.comment.entity.Comment;
import com.example.demo.src.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentReq {
    private Long articleId; // 코멘트를 추가할 게시글 ID
    private String content; // 코멘트 내용

    public Comment toEntity(Article article, User user) {
        return Comment.builder()
                .article(article)
                .user(user)
                .content(content)
                .build();
    }
}
