package com.example.demo.src.admin.model;


import com.example.demo.common.enums.ReportStatus;
import com.example.demo.src.report.entity.Report;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportRes {

    private Long  reportId;
    private  Long reportArticleId;
    private  String reportArticleContent;
    private  Long reportUserId;
    private  String reportUserName;
    private  String reportReason;
    private ReportStatus reportStatus;


    public ReportRes(Report report) {
        this.reportId = report.getId();
        this.reportArticleId = report.getArticle().getId();
        this.reportArticleContent = report.getArticle().getContent();
        this.reportUserId = report.getReporter().getId();
        this.reportUserName = report.getReporter().getName();
        this.reportReason = report.getReason();
        this.reportStatus = report.getStatus();
    }
}
