package com.example.kakaoonboarding.service;

import com.example.kakaoonboarding.dto.request.ReportGenerateRequest;
import com.example.kakaoonboarding.dto.response.ReportGenerateResponse;
import com.example.kakaoonboarding.dto.response.ReportListResponse;
import com.example.kakaoonboarding.dto.response.ReportStatusResponse;
import com.example.kakaoonboarding.entity.Report;
import com.example.kakaoonboarding.entity.ReportStatus;
import com.example.kakaoonboarding.entity.ReportType;
import com.example.kakaoonboarding.repository.ReportRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReportService {

    private final ReportRepository reportRepository;
    private static final String REPORT_DIR = "reports/";
    private static final Map<String, Report> pendingReports = new HashMap<>();

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    /**
     * 보고서 목록 조회
     */
    @Transactional(readOnly = true)
    public ReportListResponse getReportList(ReportType type, ReportStatus status) {
        List<Report> reports;

        if (type != null && status != null) {
            reports = reportRepository.findByTypeOrderByCreatedAtDesc(type).stream()
                    .filter(r -> r.getStatus() == status)
                    .collect(Collectors.toList());
        } else if (type != null) {
            reports = reportRepository.findByTypeOrderByCreatedAtDesc(type);
        } else if (status != null) {
            reports = reportRepository.findByStatusOrderByCreatedAtDesc(status);
        } else {
            reports = reportRepository.findAllByOrderByCreatedAtDesc();
        }

        List<ReportListResponse.ReportDto> data = reports.stream()
                .map(r -> new ReportListResponse.ReportDto(
                        String.valueOf(r.getId()),
                        r.getTitle(),
                        formatPeriod(r.getStartDate(), r.getEndDate()),
                        r.getType(),
                        formatFileSize(r.getFileSize()),
                        r.getStatus(),
                        r.getCreatedAt() != null ? r.getCreatedAt().toLocalDate().toString() : "",
                        "/api/reports/download/" + r.getId(),
                        r.getExpiresAt() != null ? r.getExpiresAt().toLocalDate().toString() : ""
                ))
                .collect(Collectors.toList());

        return new ReportListResponse(data);
    }

    /**
     * 보고서 다운로드 리소스 가져오기
     */
    @Transactional(readOnly = true)
    public Resource getReportResource(Long reportId) throws IOException {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("보고서를 찾을 수 없습니다"));

        if (report.getStatus() != ReportStatus.READY) {
            throw new RuntimeException("보고서가 아직 생성 중이거나 생성에 실패했습니다");
        }

        Path filePath = Paths.get(report.getFilePath());
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("보고서 파일을 읽을 수 없습니다");
        }

        return resource;
    }

    /**
     * 보고서 파일명 가져오기
     */
    @Transactional(readOnly = true)
    public String getReportFileName(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("보고서를 찾을 수 없습니다"));
        return report.getFileName();
    }

    /**
     * 보고서 생성 요청
     */
    public ReportGenerateResponse generateReport(ReportGenerateRequest request) {
        // 요청 ID 생성
        String requestId = "REQ-" + System.currentTimeMillis();

        // 보고서 엔티티 생성
        Report report = new Report();
        report.setType(request.getType());
        report.setStartDate(LocalDate.parse(request.getPeriod().getStartDate()));
        report.setEndDate(LocalDate.parse(request.getPeriod().getEndDate()));
        report.setStatus(ReportStatus.GENERATING);

        String title = String.format("%s %s 탄소 배출 보고서",
                formatPeriod(report.getStartDate(), report.getEndDate()),
                getReportTypeLabel(request.getType()));
        report.setTitle(title);

        Report saved = reportRepository.save(report);
        pendingReports.put(requestId, saved);

        // 비동기 처리 시뮬레이션 (실제로는 별도 스레드나 메시지 큐 사용)
        simulateReportGeneration(requestId, saved.getId(), request.getFormat());

        LocalDateTime expectedCompletionTime = LocalDateTime.now().plusMinutes(5);

        return new ReportGenerateResponse(
                requestId,
                "generating",
                "보고서 생성이 시작되었습니다",
                300,  // 5분
                "admin@company.com",
                expectedCompletionTime
        );
    }

    /**
     * 보고서 생성 상태 조회
     */
    @Transactional(readOnly = true)
    public ReportStatusResponse getReportStatus(String requestId) {
        Report report = pendingReports.get(requestId);

        if (report == null) {
            throw new RuntimeException("요청을 찾을 수 없습니다");
        }

        // 데이터베이스에서 최신 상태 조회
        Report current = reportRepository.findById(report.getId())
                .orElseThrow(() -> new RuntimeException("보고서를 찾을 수 없습니다"));

        String status = current.getStatus().name().toLowerCase();
        Integer progress = current.getStatus() == ReportStatus.READY ? 100 :
                          current.getStatus() == ReportStatus.GENERATING ? 50 : 0;

        String message = current.getStatus() == ReportStatus.READY ?
                        "보고서 생성이 완료되었습니다" :
                        current.getStatus() == ReportStatus.GENERATING ?
                        "보고서 생성 중입니다" : "보고서 생성에 실패했습니다";

        return new ReportStatusResponse(
                requestId,
                status,
                progress,
                String.valueOf(current.getId()),
                message,
                formatFileSize(current.getFileSize()),
                current.getCreatedAt(),
                current.getStatus() == ReportStatus.READY ?
                        "/api/reports/download/" + current.getId() : null
        );
    }

    /**
     * 보고서 생성 시뮬레이션 (실제로는 별도 작업자 사용)
     */
    private void simulateReportGeneration(String requestId, Long reportId, String format) {
        new Thread(() -> {
            try {
                // 5초 대기 (실제 생성 시뮬레이션)
                Thread.sleep(5000);

                Report report = reportRepository.findById(reportId).orElse(null);
                if (report != null) {
                    // 더미 파일 생성
                    Path reportPath = Paths.get(REPORT_DIR);
                    if (!Files.exists(reportPath)) {
                        Files.createDirectories(reportPath);
                    }

                    String extension = "pdf".equals(format) ? "pdf" : "xlsx";
                    String fileName = String.format("report-%d.%s", reportId, extension);
                    Path filePath = reportPath.resolve(fileName);

                    // 더미 파일 생성
                    Files.write(filePath, "Dummy report content".getBytes());

                    report.setStatus(ReportStatus.READY);
                    report.setFileName(fileName);
                    report.setFilePath(filePath.toString());
                    report.setFileSize(Files.size(filePath));
                    report.setCompletedAt(LocalDateTime.now());
                    report.setExpiresAt(LocalDateTime.now().plusMonths(1));

                    reportRepository.save(report);

                    System.out.println("보고서 생성 완료: " + requestId);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Report report = reportRepository.findById(reportId).orElse(null);
                if (report != null) {
                    report.setStatus(ReportStatus.FAILED);
                    report.setErrorMessage(e.getMessage());
                    reportRepository.save(report);
                }
            }
        }).start();
    }

    /**
     * 기간 포맷팅
     */
    private String formatPeriod(LocalDate startDate, LocalDate endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return startDate.format(formatter) + " - " + endDate.format(formatter);
    }

    /**
     * 파일 크기 포맷팅
     */
    private String formatFileSize(Long bytes) {
        if (bytes == null) {
            return "0 B";
        }
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.1f KB", bytes / 1024.0);
        } else {
            return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        }
    }

    /**
     * 보고서 유형 라벨
     */
    private String getReportTypeLabel(ReportType type) {
        return switch (type) {
            case MONTHLY -> "월간";
            case QUARTERLY -> "분기";
            case ANNUAL -> "연간";
            case CUSTOM -> "맞춤";
        };
    }
}
