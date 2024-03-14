package com.example.demo.src.article.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.common.enums.ArticleStatus;
import com.example.demo.src.attachment.entity.Attachment;
import com.example.demo.src.comment.entity.Comment;
import com.example.demo.src.user.entity.User;
import lombok.*;

import javax.persistence.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ARTICLE")
@Getter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private String location; // nullable, 위치 정보가 항상 있는 것은 아님

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArticleStatus status; // 예: ACTIVE, INACTIVE, DELETED 등

    @Column(nullable = false)
    private int reportCount = 0; // 신고 수, 기본값 0


    @Column(nullable = false)
    private Long favoriteCount = 0L; // Initialized to avoid null


    // 작성자와의 연관 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 작성자


    // 댓글과의 연관 관계 설정 예시 (실제 구현에 따라 다를 수 있음)
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();


    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments = new ArrayList<>();



    @Builder
    public Article(String content, String location, User user) {
        this.content = content;
        this.location = location;
        this.user = user;
        this.status = ArticleStatus.ACTIVE; // 기본 상태 설정
    }

}
