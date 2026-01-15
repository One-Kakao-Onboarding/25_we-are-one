package com.example.kakaoonboarding.controller;

import com.example.kakaoonboarding.dto.response.*;
import com.example.kakaoonboarding.service.DashboardService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * 통합 대시보드 데이터 조회
     * GET /api/dashboard/overview
     */
    @GetMapping("/overview")
    public ResponseEntity<DashboardOverviewResponse> getOverview(
            @RequestParam(defaultValue = "month") String period) {
        try {
            DashboardOverviewResponse response = dashboardService.getOverview(period);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 월별 배출 추이 조회
     * GET /api/dashboard/monthly-trend
     */
    @GetMapping("/monthly-trend")
    public ResponseEntity<MonthlyTrendResponse> getMonthlyTrend(
            @RequestParam String startMonth,
            @RequestParam String endMonth) {
        try {
            MonthlyTrendResponse response = dashboardService.getMonthlyTrend(startMonth, endMonth);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 카카오T 데이터 동기화
     * POST /api/dashboard/sync-kakao-t
     */
    @PostMapping("/sync-kakao-t")
    public ResponseEntity<Map<String, Object>> syncKakaoT() {
        try {
            Map<String, Object> response = dashboardService.syncKakaoT();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 카카오T 자동 연동 데이터 조회
     * GET /api/dashboard/kakao-t-data
     */
    @GetMapping("/kakao-t-data")
    public ResponseEntity<KakaoTDataResponse> getKakaoTData(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Integer offset) {
        try {
            KakaoTDataResponse response = dashboardService.getKakaoTData(
                    startDate, endDate, department, limit, offset);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 부서별 데이터 조회
     * GET /api/dashboard/department-stats
     */
    @GetMapping("/department-stats")
    public ResponseEntity<DepartmentStatsResponse> getDepartmentStats(
            @RequestParam(defaultValue = "month") String period) {
        try {
            DepartmentStatsResponse response = dashboardService.getDepartmentStats(period);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
