package com.example.demo.src.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
