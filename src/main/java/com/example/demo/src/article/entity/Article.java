package com.example.demo.src.article.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.common.enums.ArticleStatus;
import com.example.demo.src.comment.entity.Comment;
import com.example.demo.src.report.entity.Report;
import com.example.demo.src.user.entity.User;
import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ARTICLE")
@Getter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Audited

public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 100, columnDefinition = "TEXT")
    private String content;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArticleStatus status; // 예:활성, 비활성, 삭제됨 등

    @Column(nullable = false)
    private int reportCount = 0; // 신고 수, 기본값 0


    @Column(nullable = false)
    private Long favoriteCount = 0L;

    // 작성자와의 연관 관계 설정
    @ManyToOne
    @JoinColumn(name = "user_id") // Changed to match the DB schema
    private User author;


    @ElementCollection
    private List<String> images = new ArrayList<>();
    @ElementCollection
    private List<String> videos = new ArrayList<>();




    // 댓글과의 연관 관계 설정 예시 (실제 구현에 따라 다를 수 있음)
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();


    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();


    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();


    @Builder
    public Article(String content, User author, List<String> images,List<String> videos, ArticleStatus status) {
        this.content = content;
        this.author = author;
        this.images = images;
        this.status = status;
        this.videos= videos;
    }
    // Setter 메서드 수동 추가
    public void setContent(String content) {
        this.content = content;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setVideos(List<String> videos) {
        this.videos=videos;
    }

    public void setReportCount() {
        this.reportCount+=1;
    }

    public void setStatus(ArticleStatus status) {
        this.status= status;
    }

    public void increaseFavoriteCount() {
        this.favoriteCount += 1;
    }

    public void decreaseFavoriteCount() {
        this.favoriteCount = Math.max(0, this.favoriteCount - 1);
    }
}
