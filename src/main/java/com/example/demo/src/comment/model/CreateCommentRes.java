package com.example.demo.src.comment.model;

import com.example.demo.src.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentRes {
    private Long commentId;
    private String createdAt;

    public CreateCommentRes(Comment comment){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        this.commentId = comment.getId();
        this.createdAt = comment.getCreatedAt().format(formatter);
    }
}
