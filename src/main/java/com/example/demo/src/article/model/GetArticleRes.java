package com.example.demo.src.article.model;

import com.example.demo.common.enums.ArticleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetArticleRes {
    private Long id;
    private String content;
    private ArticleStatus status;
    private int reportCount;
    private Long favoriteCount;
    private String authorName; // 작성자 이름 또는 식별 정보
    private List<String> images;
    private List<String> videos;

    // 추가적으로 필요한 정보가 있다면 여기에 선언


}