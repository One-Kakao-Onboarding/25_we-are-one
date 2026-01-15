package com.example.kakaoonboarding.controller;

import com.example.kakaoonboarding.dto.request.ReportGenerateRequest;
import com.example.kakaoonboarding.dto.response.ReportGenerateResponse;
import com.example.kakaoonboarding.dto.response.ReportListResponse;
import com.example.kakaoonboarding.dto.response.ReportStatusResponse;
import com.example.kakaoonboarding.entity.ReportStatus;
import com.example.kakaoonboarding.entity.ReportType;
import com.example.kakaoonboarding.service.ReportService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * 보고서 목록 조회
     * GET /api/reports/list
     */
    @GetMapping("/list")
    public ResponseEntity<ReportListResponse> getReportList(
            @RequestParam(required = false) ReportType type,
            @RequestParam(required = false) ReportStatus status) {
        try {
            ReportListResponse response = reportService.getReportList(type, status);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 보고서 다운로드
     * GET /api/reports/download/{reportId}
     */
    @GetMapping("/download/{reportId}")
    public ResponseEntity<Resource> downloadReport(@PathVariable Long reportId) {
        try {
            Resource resource = reportService.getReportResource(reportId);
            String fileName = reportService.getReportFileName(reportId);

            // 파일 확장자에 따라 적절한 Content-Type 설정
            String contentType = fileName.endsWith(".pdf") ?
                    "application/pdf" : "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 새 보고서 생성 요청
     * POST /api/reports/generate
     */
    @PostMapping("/generate")
    public ResponseEntity<ReportGenerateResponse> generateReport(
            @RequestBody ReportGenerateRequest request) {
        try {
            ReportGenerateResponse response = reportService.generateReport(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 보고서 생성 상태 조회
     * GET /api/reports/status/{requestId}
     */
    @GetMapping("/status/{requestId}")
    public ResponseEntity<ReportStatusResponse> getReportStatus(@PathVariable String requestId) {
        try {
            ReportStatusResponse response = reportService.getReportStatus(requestId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
