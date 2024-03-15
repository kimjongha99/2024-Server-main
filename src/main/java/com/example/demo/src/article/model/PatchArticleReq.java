package com.example.demo.src.article.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatchArticleReq {

    private Long userId;
    private String content;
    private List<String> images;

}
