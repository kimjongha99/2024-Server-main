package com.example.demo.src.article;

import com.example.demo.src.article.entity.Article;
import com.example.demo.src.article.entity.Like;
import com.example.demo.src.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserAndArticle(User user, Article article);
}