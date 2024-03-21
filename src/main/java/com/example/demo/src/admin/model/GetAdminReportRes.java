package com.example.demo.src.admin.model;

import com.example.demo.src.report.entity.Report;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetAdminReportRes {
    private List<ReportRes> reports;

    Integer listSize;
    Integer totalPage;
    Long totalElements;
    boolean isFirst;
    boolean isLast;


    public GetAdminReportRes(Page<Report> report) {
        this.reports = report.map(ReportRes::new).getContent();
        this.listSize = report.getNumberOfElements();
        this.totalPage = report.getTotalPages();
        this.totalElements = report.getTotalElements();
        this.isFirst = report.isFirst();
        this.isLast = report.isLast();

    }
}
