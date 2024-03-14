package com.example.demo.src.attachment.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.common.enums.AttachmentType;
import com.example.demo.src.article.entity.Article;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ATTACHMENT")
@Getter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자는 보호된 접근으로 유지
public class Attachment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filePath; // 파일 경로

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttachmentType type; // 파일 종류

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article; // 첨부파일이 속한 아티클

    public Attachment(String filePath, AttachmentType type, Article article) {
        this.filePath = filePath;
        this.type = type;
        this.article = article;
    }
}
