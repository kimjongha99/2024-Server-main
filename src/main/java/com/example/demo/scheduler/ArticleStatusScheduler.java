package com.example.demo.scheduler;

import com.example.demo.common.enums.ArticleStatus;
import com.example.demo.src.article.ArticleRepository;
import com.example.demo.src.article.entity.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@EnableScheduling

public class ArticleStatusScheduler {

    private final ArticleRepository articleRepository;
    // 매일 자정(00:00)에 실행되는 스케줄러
    @Scheduled(cron = "0 0 0 * * ?")
    public void deactivateArticlesWithHighReports() {
        System.out.println("스케줄러 실행 시작");

        List<Article> articles = articleRepository.findByReportCountGreaterThanEqualAndStatus(1, ArticleStatus.ACTIVE);
        for (Article article : articles) {
            article.setStatus(ArticleStatus.INACTIVE);
            articleRepository.save(article);
        }
        System.out.println("스케줄러 실행 완료");

    }
}