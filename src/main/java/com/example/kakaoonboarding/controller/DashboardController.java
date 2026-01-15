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

    /**
     * 연도별 배출량 비교
     * GET /api/dashboard/year-comparison?year1=2019&year2=2026
     */
    @GetMapping("/year-comparison")
    public ResponseEntity<YearComparisonResponse> compareYears(
            @RequestParam Integer year1,
            @RequestParam Integer year2) {
        try {
            YearComparisonResponse response = dashboardService.compareYears(year1, year2);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 연도별 추이 조회
     * GET /api/dashboard/yearly-trend?fromYear=2019&toYear=2026
     */
    @GetMapping("/yearly-trend")
    public ResponseEntity<YearlyTrendResponse> getYearlyTrend(
            @RequestParam Integer fromYear,
            @RequestParam Integer toYear) {
        try {
            YearlyTrendResponse response = dashboardService.getYearlyTrend(fromYear, toYear);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 포인트 요약 조회
     * GET /api/dashboard/points-summary?startDate=2024-01-01&endDate=2024-01-31
     */
    @GetMapping("/points-summary")
    public ResponseEntity<PointsSummaryResponse> getPointsSummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            // 기본값: 이번 달
            if (startDate == null || endDate == null) {
                LocalDate now = LocalDate.now();
                startDate = now.withDayOfMonth(1);
                endDate = now.withDayOfMonth(now.lengthOfMonth());
            }

            PointsSummaryResponse response = dashboardService.getPointsSummary(startDate, endDate);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
