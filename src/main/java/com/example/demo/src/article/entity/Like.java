package com.example.demo.src.article.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.common.enums.LikeStatus;
import com.example.demo.src.user.entity.User;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Entity
@Table(name = "LIKES")

public class Like extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    private LikeStatus likeStatus;

    public void cancel(){
        this.likeStatus = LikeStatus.CANCEL;
    }


    public void add(){
        this.likeStatus = LikeStatus.ADD;
    }
    public Like(User user, Article article) {
        this.user = user;
        this.article = article;
        this.likeStatus = LikeStatus.ADD;
    }

}
