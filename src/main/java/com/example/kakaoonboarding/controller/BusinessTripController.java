package com.example.kakaoonboarding.controller;

import com.example.kakaoonboarding.dto.request.BusinessTripRequest;
import com.example.kakaoonboarding.dto.request.EmissionsEstimateRequest;
import com.example.kakaoonboarding.dto.response.BusinessTripHistoryResponse;
import com.example.kakaoonboarding.dto.response.BusinessTripResponse;
import com.example.kakaoonboarding.dto.response.EmissionsEstimateResponse;
import com.example.kakaoonboarding.entity.TripType;
import com.example.kakaoonboarding.service.BusinessTripService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/business-trip")
public class BusinessTripController {

    private final BusinessTripService businessTripService;

    public BusinessTripController(BusinessTripService businessTripService) {
        this.businessTripService = businessTripService;
    }

    /**
     * 출장 정보 등록
     * POST /api/business-trip/register
     */
    @PostMapping("/register")
    public ResponseEntity<BusinessTripResponse> register(
            @RequestPart("data") BusinessTripRequest request,
            @RequestPart(value = "receipt", required = false) MultipartFile receipt) {
        try {
            BusinessTripResponse response = businessTripService.register(request, receipt);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 출장 기록 조회
     * GET /api/business-trip/history
     */
    @GetMapping("/history")
    public ResponseEntity<BusinessTripHistoryResponse> getHistory(
            @RequestParam(required = false) String employeeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) TripType type,
            @RequestParam(required = false) Integer limit) {
        try {
            BusinessTripHistoryResponse response = businessTripService.getHistory(
                    employeeId, startDate, endDate, type, limit);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 출장 배출량 예측
     * POST /api/business-trip/estimate-emissions
     */
    @PostMapping("/estimate-emissions")
    public ResponseEntity<EmissionsEstimateResponse> estimateEmissions(
            @RequestBody EmissionsEstimateRequest request) {
        try {
            EmissionsEstimateResponse response = businessTripService.estimateEmissions(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
