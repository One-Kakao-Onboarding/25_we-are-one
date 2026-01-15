package com.example.kakaoonboarding.repository;

import com.example.kakaoonboarding.entity.Report;
import com.example.kakaoonboarding.entity.ReportStatus;
import com.example.kakaoonboarding.entity.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    // 보고서 유형별 조회
    List<Report> findByTypeOrderByCreatedAtDesc(ReportType type);

    // 보고서 상태별 조회
    List<Report> findByStatusOrderByCreatedAtDesc(ReportStatus status);

    // 모든 보고서 조회 (최근순)
    List<Report> findAllByOrderByCreatedAtDesc();
}
