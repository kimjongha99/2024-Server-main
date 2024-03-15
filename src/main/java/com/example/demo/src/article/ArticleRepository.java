package com.example.demo.src.article;

import com.example.demo.common.enums.ArticleStatus;
import com.example.demo.src.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Long> {
    Optional<Article> findByIdAndStatus(Long articleId, ArticleStatus active);
}
