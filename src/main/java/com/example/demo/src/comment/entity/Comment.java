package com.example.demo.src.comment.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.article.entity.Article;
import com.example.demo.src.user.entity.User;
import lombok.*;

import javax.persistence.*;

@Getter
@EqualsAndHashCode(callSuper = false)

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "comment")
@Table(name = "comment")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private  String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    // 댓글과 사용자(User) 간의 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;





}