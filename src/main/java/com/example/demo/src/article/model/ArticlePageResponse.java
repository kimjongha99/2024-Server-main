package com.example.demo.src.article.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticlePageResponse implements Serializable {
    private List<GetArticlePreviewRes> articles; // 현재 페이지의 게시글 목록
    private int currentPage; // 현재 페이지 번호
    private int totalItems; // 총 게시글 수
    private int totalPages; // 총 페이지 수

}
