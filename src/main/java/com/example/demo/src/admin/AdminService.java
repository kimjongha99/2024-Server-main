package com.example.demo.src.admin;


import com.example.demo.common.enums.ArticleStatus;
import com.example.demo.common.enums.ReportStatus;
import com.example.demo.common.enums.UserState;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.src.admin.model.*;
import com.example.demo.src.article.ArticleRepository;
import com.example.demo.src.article.entity.Article;
import com.example.demo.src.report.ReportRepository;
import com.example.demo.src.report.entity.Report;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    @PersistenceContext
    private EntityManager entityManager;

    private final UserRepository userRepository;
    private final ReportRepository reportRepository;

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public AdminUserDetailRes getAdminUserDetails(Long userId) {
        // 사용자 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND)); // 사용자를 찾을 수 없을 때 예외 처리
        // 조회된 사용자 정보를 DTO로 변환
        return new AdminUserDetailRes(user);

    }



    @Transactional(readOnly = true)
    public GetAdminUserRes getRecentLoggedInUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "lastLoginAt"));
        Page<User> userPage = userRepository.findAll(pageable);
        return  new GetAdminUserRes(userPage);

    }
    @Transactional(readOnly = true)
    public GetAdminReportRes getReports(int page, int size, String status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Report> reports;

        if (status != null) {
            ReportStatus reportStatus = ReportStatus.valueOf(status.toUpperCase());
            reports = reportRepository.findByStatus(reportStatus, pageable);
        } else {
            reports = reportRepository.findAll(pageable);
        }

        return  new GetAdminReportRes(reports);
    }


    @Transactional
    public void updateReport(Long reportId) {
        // 신고 ID를 이용하여 신고 객체 조회
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.REQUEST_ERROR));
        // 신고 객체의 상태 업데이트
        report.setStatus(ReportStatus.RESOLVED);
        reportRepository.save(report);

    }


    @Transactional
    public void updateArticleStatus(Long articleId, ArticleStatus status) {
        // 아티클 ID로 아티클 조회
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.ARTICLE_NOT_FOUND));
        // 아티클 상태 업데이트
        article.setStatus(status);
        // 변경 사항 저장
        articleRepository.save(article);

    }

    @Transactional
    public void updateUserStatus(Long userId, UserState status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        user.setStatus(status);
        userRepository.save(user);
    }

    public List<UserAuditResponse> findUserAudits(Long userId) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        AuditQuery query = auditReader.createQuery().forRevisionsOfEntity(User.class, false, true);
        query.add(AuditEntity.id().eq(userId));
        List<Object[]> results = query.getResultList();

        return results.stream().map(obj -> {
            User user = (User) obj[0];
            DefaultRevisionEntity revisionEntity = (DefaultRevisionEntity) obj[1];
            RevisionType revisionType = (RevisionType) obj[2];

            UserAuditResponse response = new UserAuditResponse();
            response.setUserId(user.getId());
            response.setRevision(revisionEntity.getId());
            response.setChangedAt(String.valueOf(revisionEntity.getRevisionDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()));
            response.setRevisionType(revisionType.name());

            return response;
        }).collect(Collectors.toList());
    }

}


