package com.example.demo.src.article.model;


import com.example.demo.common.enums.ArticleStatus;
import com.example.demo.src.article.entity.Article;
import com.example.demo.src.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostArticleReq {


    @NotBlank(message = "작성자 정보는 필수 입력사항 입니다.")
    Long userId;

    @Size(min = 0, max = 500, message = "게시글은 500자 이내로 작성해주세요.")
    String content;

    List<String> images;

    List<String> videos;
    public Article toEntity(User author) {
        return Article.builder()
                .author(author)
                .content(this.content)
                .images(this.images)
                .videos(this.videos)
                .status(ArticleStatus.ACTIVE) // Assuming ACTIVE is a default status
                .build();
    }
}
