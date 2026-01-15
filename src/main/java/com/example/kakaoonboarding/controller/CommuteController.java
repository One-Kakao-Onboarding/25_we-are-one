package com.example.kakaoonboarding.controller;

import com.example.kakaoonboarding.dto.request.CommuteCheckInRequest;
import com.example.kakaoonboarding.dto.response.CommuteCheckInResponse;
import com.example.kakaoonboarding.dto.response.CommuteHistoryResponse;
import com.example.kakaoonboarding.dto.response.CommuteStatisticsResponse;
import com.example.kakaoonboarding.service.CommuteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/commute")
public class CommuteController {

    private final CommuteService commuteService;

    public CommuteController(CommuteService commuteService) {
        this.commuteService = commuteService;
    }

    /**
     * 출근 체크인 등록
     * POST /api/commute/check-in
     */
    @PostMapping("/check-in")
    public ResponseEntity<CommuteCheckInResponse> checkIn(
            @RequestBody CommuteCheckInRequest request,
            HttpSession session) {
        try {
            CommuteCheckInResponse response = commuteService.checkIn(request, session);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 출근 기록 조회
     * GET /api/commute/history
     */
    @GetMapping("/history")
    public ResponseEntity<CommuteHistoryResponse> getHistory(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Integer limit,
            HttpSession session) {
        try {
            CommuteHistoryResponse response = commuteService.getHistory(startDate, endDate, limit, session);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 출근 통계 조회
     * GET /api/commute/statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<CommuteStatisticsResponse> getStatistics(
            @RequestParam(defaultValue = "month") String period,
            HttpSession session) {
        try {
            CommuteStatisticsResponse response = commuteService.getStatistics(period, session);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
