package com.example.demo.src.comment;

import com.example.demo.src.article.entity.Article;
import com.example.demo.src.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CommentRepository extends JpaRepository<Comment,Long> {
    Page<Comment> findByArticleId(Long articleId, Pageable pageable);
}
