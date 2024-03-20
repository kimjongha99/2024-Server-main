package com.example.demo.src.report.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.common.enums.ReportStatus;
import com.example.demo.src.article.entity.Article;
import com.example.demo.src.user.entity.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "REPORT")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)

public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article; // 신고된 게시물

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User reporter; // 신고한 사용자

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason; // 신고 이유

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status = ReportStatus.PENDING; // 신고 처리 상태

    @Builder
    public Report(Article article, User reporter, String reason) {
        this.article = article;
        this.reporter = reporter;
        this.reason = reason;
        this.status = ReportStatus.PENDING;
    }

    public void setStatus(ReportStatus reportStatus) {
        this.status =reportStatus;
    }
}