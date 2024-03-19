package com.example.demo.src.comment.model;

import com.example.demo.src.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetCommentRes {
    List<CommentInfo> comments;

    Integer listSize;
    Integer totalPage;
    Long totalElements;
    boolean isFirst;
    boolean isLast;


    public GetCommentRes(Page<Comment> comments) {
        this.comments = comments.map(CommentInfo::new).getContent();
        this.listSize = comments.getNumberOfElements();
        this.totalPage = comments.getTotalPages();
        this.totalElements = comments.getTotalElements();
        this.isFirst = comments.isFirst();
        this.isLast = comments.isLast();
    }
}
