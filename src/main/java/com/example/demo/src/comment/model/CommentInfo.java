package com.example.demo.src.comment.model;

import com.example.demo.src.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentInfo {
    private Long commentId;
    private String content;
    private Long authorId;
    private String authorName;


    public CommentInfo(Comment comment) {
        this.commentId = comment.getId();
        this.content = comment.getContent();
        this.authorId = comment.getUser().getId();
        this.authorName = comment.getUser().getName();
    }
}
