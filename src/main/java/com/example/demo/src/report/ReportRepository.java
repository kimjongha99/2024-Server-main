package com.example.demo.src.report;


import com.example.demo.common.enums.ReportStatus;
import com.example.demo.src.report.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Page<Report> findByStatus(ReportStatus status, Pageable pageable);
    Page<Report> findAll(Pageable pageable);

}
