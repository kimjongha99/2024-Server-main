package com.example.demo.src.article.model;

import com.example.demo.src.article.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostArticleRes {

    private Long articleId;
    private Long userId;
    private String content;
    private String createdAt; // Changed to String to hold the formatted date-time


    public PostArticleRes(Article article) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.articleId = article.getId();
        this.userId = article.getAuthor().getId();
        this.content = article.getContent();
        this.createdAt = article.getCreatedAt().format(formatter); // Format the LocalDateTime
    }
}
