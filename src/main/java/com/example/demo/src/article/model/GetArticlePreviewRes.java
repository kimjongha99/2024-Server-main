package com.example.demo.src.article.model;


import lombok.*;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class GetArticlePreviewRes {
    private Long articleId;
    private String title;
    private String contentPreview;// 콘텐츠의 짧은 스니펫 또는 미리보기
    private String authorName;
    private List<String> imageUrls;// 각 기사에 여러 이미지가 있

}
