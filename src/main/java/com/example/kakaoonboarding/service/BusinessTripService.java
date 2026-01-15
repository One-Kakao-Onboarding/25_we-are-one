package com.example.kakaoonboarding.service;

import com.example.kakaoonboarding.config.EmissionFactors;
import com.example.kakaoonboarding.dto.Coordinates;
import com.example.kakaoonboarding.dto.request.BusinessTripRequest;
import com.example.kakaoonboarding.dto.request.EmissionsEstimateRequest;
import com.example.kakaoonboarding.dto.response.BusinessTripHistoryResponse;
import com.example.kakaoonboarding.dto.response.BusinessTripResponse;
import com.example.kakaoonboarding.dto.response.EmissionsEstimateResponse;
import com.example.kakaoonboarding.entity.BusinessTrip;
import com.example.kakaoonboarding.entity.TripType;
import com.example.kakaoonboarding.repository.BusinessTripRepository;
import com.example.kakaoonboarding.service.KakaoLocalService;
import com.example.kakaoonboarding.service.KakaoMobilityService;
import com.example.kakaoonboarding.dto.kakao.KakaoMobilityResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class BusinessTripService {

    private final BusinessTripRepository businessTripRepository;
    private final KakaoLocalService kakaoLocalService;
    private final KakaoMobilityService kakaoMobilityService;
    private static final String UPLOAD_DIR = "uploads/receipts/";

    public BusinessTripService(BusinessTripRepository businessTripRepository,
                              KakaoLocalService kakaoLocalService,
                              KakaoMobilityService kakaoMobilityService) {
        this.businessTripRepository = businessTripRepository;
        this.kakaoLocalService = kakaoLocalService;
        this.kakaoMobilityService = kakaoMobilityService;
    }

    /**
     * 출장 정보 등록
     */
    public BusinessTripResponse register(BusinessTripRequest request, MultipartFile receipt) {
        // 임시 직원 정보 (추후 인증 시스템 구현 시 변경)
        String employeeId = request.getEmployeeId() != null ? request.getEmployeeId() : "EMP001";
        String employeeName = request.getEmployeeName() != null ? request.getEmployeeName() : "김직원";
        String department = request.getDepartment() != null ? request.getDepartment() : "개발팀";

        // 영수증 파일 처리
        String receiptFileName = null;
        String receiptFilePath = null;
        if (receipt != null && !receipt.isEmpty()) {
            try {
                receiptFileName = saveReceipt(receipt, employeeId);
                receiptFilePath = UPLOAD_DIR + receiptFileName;
            } catch (IOException e) {
                System.err.println("영수증 파일 저장 실패: " + e.getMessage());
            }
        }

        // 엔티티 생성 및 저장
        BusinessTrip trip = new BusinessTrip();
        trip.setEmployeeId(employeeId);
        trip.setEmployeeName(employeeName);
        trip.setDepartment(department);
        trip.setDate(LocalDate.parse(request.getDate()));
        trip.setType(request.getType());
        trip.setDeparture(request.getDeparture());
        trip.setArrival(request.getArrival());
        trip.setDistance(request.getDistance());
        trip.setEmissions(request.getEmissions());
        trip.setReceiptFileName(receiptFileName);
        trip.setReceiptFilePath(receiptFilePath);

        BusinessTrip saved = businessTripRepository.save(trip);

        // 이번 달 출장 통계 계산
        YearMonth currentMonth = YearMonth.now();
        LocalDate startOfMonth = currentMonth.atDay(1);
        LocalDate endOfMonth = currentMonth.atEndOfMonth();

        List<BusinessTrip> monthlyTrips = businessTripRepository
                .findByEmployeeIdAndDateBetweenOrderByDateDesc(employeeId, startOfMonth, endOfMonth);

        Integer totalTrips = monthlyTrips.size();
        Double totalEmissions = businessTripRepository
                .sumEmissionsByEmployeeIdAndDateBetween(employeeId, startOfMonth, endOfMonth);

        return new BusinessTripResponse(
                "BT-" + saved.getId(),
                "success",
                "출장 정보가 등록되었습니다",
                saved.getCreatedAt(),
                totalTrips,
                totalEmissions
        );
    }

    /**
     * 출장 기록 조회
     */
    @Transactional(readOnly = true)
    public BusinessTripHistoryResponse getHistory(String employeeId, LocalDate startDate,
                                                 LocalDate endDate, TripType type, Integer limit) {
        // 임시 직원 ID (추후 인증 시스템 구현 시 변경)
        if (employeeId == null) {
            employeeId = "EMP001";
        }

        // 날짜 범위 설정 (기본값: 이번 달)
        if (startDate == null || endDate == null) {
            YearMonth currentMonth = YearMonth.now();
            startDate = currentMonth.atDay(1);
            endDate = currentMonth.atEndOfMonth();
        }

        // final 변수로 복사 (람다 표현식에서 사용)
        final LocalDate finalStartDate = startDate;
        final LocalDate finalEndDate = endDate;

        // 기록 조회
        List<BusinessTrip> trips;
        if (type != null) {
            trips = businessTripRepository.findByEmployeeIdAndType(employeeId, type);
            trips = trips.stream()
                    .filter(t -> !t.getDate().isBefore(finalStartDate) && !t.getDate().isAfter(finalEndDate))
                    .sorted(Comparator.comparing(BusinessTrip::getDate).reversed())
                    .collect(Collectors.toList());
        } else {
            trips = businessTripRepository
                    .findByEmployeeIdAndDateBetweenOrderByDateDesc(employeeId, finalStartDate, finalEndDate);
        }

        // limit 적용
        if (limit != null && limit > 0 && trips.size() > limit) {
            trips = trips.subList(0, limit);
        }

        // DTO 변환
        List<BusinessTripHistoryResponse.BusinessTripDto> data = trips.stream()
                .map(t -> new BusinessTripHistoryResponse.BusinessTripDto(
                        "BT-" + t.getId(),
                        t.getDate().toString(),
                        t.getType(),
                        t.getDeparture(),
                        t.getArrival(),
                        t.getDistance(),
                        t.getEmissions(),
                        t.getReceiptFileName()
                ))
                .collect(Collectors.toList());

        // 통계 계산
        Integer totalTrips = trips.size();
        Double totalDistance = businessTripRepository.sumDistanceByEmployeeIdAndDateBetween(
                employeeId, finalStartDate, finalEndDate);
        Double totalEmissions = businessTripRepository.sumEmissionsByEmployeeIdAndDateBetween(
                employeeId, finalStartDate, finalEndDate);

        // 유형별 통계
        Map<TripType, BusinessTripHistoryResponse.TypeStats> byType = new HashMap<>();
        List<Object[]> typeStats = businessTripRepository.getStatsByType(employeeId, finalStartDate, finalEndDate);
        for (Object[] stat : typeStats) {
            TripType tripType = (TripType) stat[0];
            Long count = (Long) stat[1];
            Double emissions = (Double) stat[2];
            byType.put(tripType, new BusinessTripHistoryResponse.TypeStats(count, emissions));
        }

        BusinessTripHistoryResponse.BusinessTripStats stats =
                new BusinessTripHistoryResponse.BusinessTripStats(
                        totalTrips, totalDistance, totalEmissions, byType
                );

        return new BusinessTripHistoryResponse(data, stats);
    }

    /**
     * 출장 배출량 예측
     */
    @Transactional(readOnly = true)
    public EmissionsEstimateResponse estimateEmissions(EmissionsEstimateRequest request) {
        try {
            // 1. 출발지와 목적지의 좌표를 가져옴
            Coordinates originCoords = kakaoLocalService.getCoordinates(request.getDeparture());
            Coordinates destCoords = kakaoLocalService.getCoordinates(request.getArrival());

            // 2. 두 좌표 사이의 거리를 계산
            KakaoMobilityResponse.Summary summary = kakaoMobilityService.getDistance(originCoords, destCoords);
            Double distance = summary.getDistance() / 1000.0;  // 미터를 킬로미터로 변환

            // 3. 교통수단별 배출 계수 적용
            Double emissionsFactor = EmissionFactors.getFactorByTripType(request.getType());
            Double estimatedEmissions = EmissionFactors.calculateEmissions(distance, emissionsFactor);

            return new EmissionsEstimateResponse(distance, estimatedEmissions, emissionsFactor);
        } catch (Exception e) {
            // 예외 발생 시 기본값 반환
            System.err.println("배출량 예측 실패: " + e.getMessage());
            e.printStackTrace();
            Double emissionsFactor = EmissionFactors.getFactorByTripType(request.getType());
            return new EmissionsEstimateResponse(0.0, 0.0, emissionsFactor);
        }
    }

    /**
     * 영수증 파일 저장
     */
    private String saveReceipt(MultipartFile file, String employeeId) throws IOException {
        // 업로드 디렉토리 생성
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 파일명 생성 (중복 방지)
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String filename = employeeId + "_" + System.currentTimeMillis() + extension;

        // 파일 저장
        Path filePath = uploadPath.resolve(filename);
        file.transferTo(filePath.toFile());

        return filename;
    }
}
