package com.example.demo.src.article;

import com.example.demo.common.enums.ArticleStatus;
import com.example.demo.common.enums.UserState;
import com.example.demo.src.article.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Long> {
    Optional<Article> findByIdAndStatus(Long articleId, ArticleStatus active);

    List<Article> findByReportCountGreaterThanEqualAndStatus(int i, ArticleStatus active);

    Page<Article> findAllByStatus(ArticleStatus active, Pageable pageable);
    // Use a JPQL query to select just the IDs
    @Query("SELECT a.id FROM Article a")
    List<Long> findAllArticleIds();
}
