package com.example.kakaoonboarding.service;

import com.example.kakaoonboarding.config.EmissionFactors;
import com.example.kakaoonboarding.dto.response.*;
import com.example.kakaoonboarding.entity.*;
import com.example.kakaoonboarding.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final CommuteRecordRepository commuteRecordRepository;
    private final BusinessTripRepository businessTripRepository;
    private final KakaoTDataRepository kakaoTDataRepository;
    private final PointsService pointsService;

    public DashboardService(CommuteRecordRepository commuteRecordRepository,
                          BusinessTripRepository businessTripRepository,
                          KakaoTDataRepository kakaoTDataRepository,
                          PointsService pointsService) {
        this.commuteRecordRepository = commuteRecordRepository;
        this.businessTripRepository = businessTripRepository;
        this.kakaoTDataRepository = kakaoTDataRepository;
        this.pointsService = pointsService;
    }

    /**
     * 통합 대시보드 데이터 조회
     */
    public DashboardOverviewResponse getOverview(String period) {
        LocalDate startDate, endDate;
        LocalDate prevStartDate, prevEndDate;

        // 기간 설정
        if ("quarter".equals(period)) {
            YearMonth currentMonth = YearMonth.now();
            int currentQuarter = (currentMonth.getMonthValue() - 1) / 3;
            startDate = LocalDate.of(currentMonth.getYear(), currentQuarter * 3 + 1, 1);
            endDate = LocalDate.of(currentMonth.getYear(), currentQuarter * 3 + 3, 1)
                    .plusMonths(1).minusDays(1);
            prevStartDate = startDate.minusMonths(3);
            prevEndDate = endDate.minusMonths(3);
        } else if ("year".equals(period)) {
            int currentYear = LocalDate.now().getYear();
            startDate = LocalDate.of(currentYear, 1, 1);
            endDate = LocalDate.of(currentYear, 12, 31);
            prevStartDate = LocalDate.of(currentYear - 1, 1, 1);
            prevEndDate = LocalDate.of(currentYear - 1, 12, 31);
        } else {
            // 기본값: 월간
            YearMonth currentMonth = YearMonth.now();
            startDate = currentMonth.atDay(1);
            endDate = currentMonth.atEndOfMonth();
            YearMonth prevMonth = currentMonth.minusMonths(1);
            prevStartDate = prevMonth.atDay(1);
            prevEndDate = prevMonth.atEndOfMonth();
        }

        // 1. 출근 데이터
        List<CommuteRecord> commuteRecords = commuteRecordRepository.findByDateBetween(startDate, endDate);
        List<CommuteRecord> prevCommuteRecords = commuteRecordRepository.findByDateBetween(prevStartDate, prevEndDate);

        // 2. 출장 데이터
        List<BusinessTrip> businessTrips = businessTripRepository.findByDateBetween(startDate, endDate);
        List<BusinessTrip> prevBusinessTrips = businessTripRepository.findByDateBetween(prevStartDate, prevEndDate);

        // 3. 카카오T 데이터
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        LocalDateTime prevStartDateTime = prevStartDate.atStartOfDay();
        LocalDateTime prevEndDateTime = prevEndDate.atTime(23, 59, 59);

        List<KakaoTData> kakaoTData = kakaoTDataRepository.findByUsageDateBetweenOrderByUsageDateDesc(
                startDateTime, endDateTime);
        List<KakaoTData> prevKakaoTData = kakaoTDataRepository.findByUsageDateBetweenOrderByUsageDateDesc(
                prevStartDateTime, prevEndDateTime);

        // 총 배출량 계산
        Double commuteEmissions = commuteRecords.stream()
                .mapToDouble(r -> r.getEmissions() != null ? r.getEmissions() : 0.0)
                .sum();
        Double businessEmissions = businessTrips.stream()
                .mapToDouble(t -> t.getEmissions() != null ? t.getEmissions() : 0.0)
                .sum();
        Double kakaoTEmissions = kakaoTData.stream()
                .mapToDouble(k -> k.getEmissions() != null ? k.getEmissions() : 0.0)
                .sum();

        Double totalEmissions = commuteEmissions + businessEmissions + kakaoTEmissions;

        // 전월 배출량 계산
        Double prevCommuteEmissions = prevCommuteRecords.stream()
                .mapToDouble(r -> r.getEmissions() != null ? r.getEmissions() : 0.0)
                .sum();
        Double prevBusinessEmissions = prevBusinessTrips.stream()
                .mapToDouble(t -> t.getEmissions() != null ? t.getEmissions() : 0.0)
                .sum();
        Double prevKakaoTEmissions = prevKakaoTData.stream()
                .mapToDouble(k -> k.getEmissions() != null ? k.getEmissions() : 0.0)
                .sum();

        Double previousMonthEmissions = prevCommuteEmissions + prevBusinessEmissions + prevKakaoTEmissions;

        // 전월 대비 변화율
        Double trend = 0.0;
        if (previousMonthEmissions > 0) {
            trend = ((totalEmissions - previousMonthEmissions) / previousMonthEmissions) * 100;
        }

        // EV 이용률 계산
        long evCount = commuteRecords.stream()
                .filter(r -> r.getVehicleType() == VehicleType.EV)
                .count();
        long totalVehicleCount = commuteRecords.stream()
                .filter(r -> r.getUsedCar() != null && r.getUsedCar())
                .count();
        Double evRatio = totalVehicleCount > 0 ? (evCount * 100.0 / totalVehicleCount) : 0.0;

        // 참여 직원 수 (중복 제거)
        Set<String> participantIds = new HashSet<>();
        commuteRecords.forEach(r -> participantIds.add(r.getEmployeeId()));
        businessTrips.forEach(t -> participantIds.add(t.getEmployeeId()));
        kakaoTData.forEach(k -> participantIds.add(k.getEmployeeId()));

        Integer participantCount = participantIds.size();
        Integer totalEmployees = 1500;  // 임시 값 (추후 직원 테이블에서 조회)

        DashboardOverviewResponse.Summary summary = new DashboardOverviewResponse.Summary(
                totalEmissions, previousMonthEmissions, trend, evRatio, participantCount, totalEmployees
        );

        // 차량별 배출량
        Map<String, Double> vehicleEmissions = new HashMap<>();
        vehicleEmissions.put("EV", commuteRecords.stream()
                .filter(r -> r.getVehicleType() == VehicleType.EV)
                .mapToDouble(r -> r.getEmissions() != null ? r.getEmissions() : 0.0)
                .sum());
        vehicleEmissions.put("Hybrid", commuteRecords.stream()
                .filter(r -> r.getVehicleType() == VehicleType.HYBRID)
                .mapToDouble(r -> r.getEmissions() != null ? r.getEmissions() : 0.0)
                .sum());
        vehicleEmissions.put("ICE", commuteRecords.stream()
                .filter(r -> r.getVehicleType() == VehicleType.ICE)
                .mapToDouble(r -> r.getEmissions() != null ? r.getEmissions() : 0.0)
                .sum());
        vehicleEmissions.put("total", commuteEmissions);

        // 항공편 배출량 (임시 분류)
        Map<String, Double> flightEmissions = new HashMap<>();
        Double flightTotal = businessTrips.stream()
                .filter(t -> t.getType() == TripType.FLIGHT)
                .mapToDouble(t -> t.getEmissions() != null ? t.getEmissions() : 0.0)
                .sum();
        flightEmissions.put("domestic", flightTotal * 0.6);  // 임시 비율
        flightEmissions.put("international", flightTotal * 0.4);  // 임시 비율
        flightEmissions.put("total", flightTotal);

        // 물류 배출량
        Map<String, Double> logisticsEmissions = new HashMap<>();
        logisticsEmissions.put("taxi", kakaoTData.stream()
                .filter(k -> k.getServiceType() == KakaoTServiceType.TAXI)
                .mapToDouble(k -> k.getEmissions() != null ? k.getEmissions() : 0.0)
                .sum());
        logisticsEmissions.put("quick", kakaoTData.stream()
                .filter(k -> k.getServiceType() == KakaoTServiceType.QUICK)
                .mapToDouble(k -> k.getEmissions() != null ? k.getEmissions() : 0.0)
                .sum());
        logisticsEmissions.put("bike", kakaoTData.stream()
                .filter(k -> k.getServiceType() == KakaoTServiceType.BIKE)
                .mapToDouble(k -> k.getEmissions() != null ? k.getEmissions() : 0.0)
                .sum());
        logisticsEmissions.put("total", kakaoTEmissions);

        return new DashboardOverviewResponse(summary, vehicleEmissions, flightEmissions, logisticsEmissions);
    }

    /**
     * 월별 배출 추이 조회
     */
    public MonthlyTrendResponse getMonthlyTrend(String startMonth, String endMonth) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth start = YearMonth.parse(startMonth, formatter);
        YearMonth end = YearMonth.parse(endMonth, formatter);

        List<MonthlyTrendResponse.MonthlyData> dataList = new ArrayList<>();

        YearMonth current = start;
        while (!current.isAfter(end)) {
            LocalDate monthStart = current.atDay(1);
            LocalDate monthEnd = current.atEndOfMonth();

            // 각 카테고리별 배출량 계산
            Double commuteEmissions = commuteRecordRepository.findByDateBetween(monthStart, monthEnd)
                    .stream()
                    .mapToDouble(r -> r.getEmissions() != null ? r.getEmissions() : 0.0)
                    .sum();

            Double businessEmissions = businessTripRepository.findByDateBetween(monthStart, monthEnd)
                    .stream()
                    .mapToDouble(t -> t.getEmissions() != null ? t.getEmissions() : 0.0)
                    .sum();

            LocalDateTime monthStartTime = monthStart.atStartOfDay();
            LocalDateTime monthEndTime = monthEnd.atTime(23, 59, 59);
            Double logisticsEmissions = kakaoTDataRepository
                    .findByUsageDateBetweenOrderByUsageDateDesc(monthStartTime, monthEndTime)
                    .stream()
                    .mapToDouble(k -> k.getEmissions() != null ? k.getEmissions() : 0.0)
                    .sum();

            Double total = commuteEmissions + businessEmissions + logisticsEmissions;

            dataList.add(new MonthlyTrendResponse.MonthlyData(
                    current.format(formatter),
                    commuteEmissions,
                    businessEmissions,
                    logisticsEmissions,
                    total
            ));

            current = current.plusMonths(1);
        }

        return new MonthlyTrendResponse(dataList);
    }

    /**
     * 카카오T 데이터 조회
     */
    public KakaoTDataResponse getKakaoTData(LocalDate startDate, LocalDate endDate,
                                           String department, Integer limit, Integer offset) {
        if (startDate == null || endDate == null) {
            YearMonth currentMonth = YearMonth.now();
            startDate = currentMonth.atDay(1);
            endDate = currentMonth.atEndOfMonth();
        }

        if (limit == null) {
            limit = 50;
        }
        if (offset == null) {
            offset = 0;
        }

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<KakaoTData> allData;
        if (department != null && !department.isEmpty()) {
            allData = kakaoTDataRepository.findByDepartmentAndUsageDateBetween(
                    department, startDateTime, endDateTime);
        } else {
            allData = kakaoTDataRepository.findByUsageDateBetweenOrderByUsageDateDesc(
                    startDateTime, endDateTime);
        }

        Long total = (long) allData.size();

        // 페이지네이션 적용
        List<KakaoTData> paginatedData = allData.stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());

        // DTO 변환
        List<KakaoTDataResponse.KakaoTDataDto> dataList = paginatedData.stream()
                .map(k -> new KakaoTDataResponse.KakaoTDataDto(
                        "KT-" + k.getId(),
                        k.getUsageDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        k.getServiceType(),
                        k.getVehicleType(),
                        k.getDistance(),
                        k.getEmissions(),
                        k.getRoute(),
                        k.getDepartment()
                ))
                .collect(Collectors.toList());

        KakaoTDataResponse.Pagination pagination = new KakaoTDataResponse.Pagination(
                total, limit, offset
        );

        return new KakaoTDataResponse(dataList, pagination);
    }

    /**
     * 부서별 통계 조회
     */
    public DepartmentStatsResponse getDepartmentStats(String period) {
        YearMonth currentMonth = YearMonth.now();
        LocalDate startDate = currentMonth.atDay(1);
        LocalDate endDate = currentMonth.atEndOfMonth();

        // 부서 목록 추출
        List<CommuteRecord> allRecords = commuteRecordRepository.findByDateBetween(startDate, endDate);
        Set<String> departments = allRecords.stream()
                .map(CommuteRecord::getDepartment)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 각 부서별 통계 계산
        List<DepartmentStatsResponse.DepartmentData> dataList = new ArrayList<>();
        for (String dept : departments) {
            Double emissions = calculateDepartmentEmissions(dept, startDate, endDate);
            Double budget = 3000.0;  // 임시 예산 (추후 부서 설정에서 조회)
            Double utilizationRate = (emissions / budget) * 100;

            // 전월 배출량 계산
            YearMonth prevMonth = currentMonth.minusMonths(1);
            Double prevEmissions = calculateDepartmentEmissions(
                    dept, prevMonth.atDay(1), prevMonth.atEndOfMonth());

            Double trend = 0.0;
            String trendDirection = "stable";
            if (prevEmissions > 0) {
                trend = ((emissions - prevEmissions) / prevEmissions) * 100;
                trendDirection = trend > 0 ? "up" : (trend < 0 ? "down" : "stable");
            }

            dataList.add(new DepartmentStatsResponse.DepartmentData(
                    dept, emissions, budget, utilizationRate, trend, trendDirection
            ));
        }

        return new DepartmentStatsResponse(dataList);
    }

    /**
     * 부서별 총 배출량 계산 (출근 + 출장 + 카카오T)
     */
    private Double calculateDepartmentEmissions(String department, LocalDate startDate, LocalDate endDate) {
        Double commuteEmissions = commuteRecordRepository
                .findByDepartmentAndDateBetween(department, startDate, endDate)
                .stream()
                .mapToDouble(r -> r.getEmissions() != null ? r.getEmissions() : 0.0)
                .sum();

        Double businessEmissions = businessTripRepository
                .findByDepartmentAndDateBetween(department, startDate, endDate)
                .stream()
                .mapToDouble(t -> t.getEmissions() != null ? t.getEmissions() : 0.0)
                .sum();

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        Double kakaoTEmissions = kakaoTDataRepository
                .findByDepartmentAndUsageDateBetween(department, startDateTime, endDateTime)
                .stream()
                .mapToDouble(k -> k.getEmissions() != null ? k.getEmissions() : 0.0)
                .sum();

        return commuteEmissions + businessEmissions + kakaoTEmissions;
    }

    /**
     * 카카오T 데이터 동기화 (임시 구현)
     */
    @Transactional
    public Map<String, Object> syncKakaoT() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "completed");
        response.put("message", "카카오T 데이터 동기화가 완료되었습니다");
        response.put("newRecords", 0);
        response.put("updatedRecords", 0);
        response.put("syncedAt", LocalDateTime.now());

        return response;
    }

    /**
     * 연도별 배출량 비교
     */
    public YearComparisonResponse compareYears(Integer year1, Integer year2) {
        // 기준 연도 데이터
        LocalDate year1Start = LocalDate.of(year1, 1, 1);
        LocalDate year1End = LocalDate.of(year1, 12, 31);
        LocalDateTime year1DateTimeStart = year1Start.atStartOfDay();
        LocalDateTime year1DateTimeEnd = year1End.atTime(23, 59, 59);

        Double year1Commute = commuteRecordRepository
                .sumEmissionsByDateBetween(year1Start, year1End);
        Double year1Business = businessTripRepository
                .sumEmissionsByDateBetween(year1Start, year1End);
        Double year1KakaoT = kakaoTDataRepository
                .sumEmissionsByUsageDateBetween(year1DateTimeStart, year1DateTimeEnd);

        Double year1Total = (year1Commute != null ? year1Commute : 0.0) +
                           (year1Business != null ? year1Business : 0.0) +
                           (year1KakaoT != null ? year1KakaoT : 0.0);

        // 비교 연도 데이터
        LocalDate year2Start = LocalDate.of(year2, 1, 1);
        LocalDate year2End = LocalDate.of(year2, 12, 31);
        LocalDateTime year2DateTimeStart = year2Start.atStartOfDay();
        LocalDateTime year2DateTimeEnd = year2End.atTime(23, 59, 59);

        Double year2Commute = commuteRecordRepository
                .sumEmissionsByDateBetween(year2Start, year2End);
        Double year2Business = businessTripRepository
                .sumEmissionsByDateBetween(year2Start, year2End);
        Double year2KakaoT = kakaoTDataRepository
                .sumEmissionsByUsageDateBetween(year2DateTimeStart, year2DateTimeEnd);

        Double year2Total = (year2Commute != null ? year2Commute : 0.0) +
                           (year2Business != null ? year2Business : 0.0) +
                           (year2KakaoT != null ? year2KakaoT : 0.0);

        // 절감량 및 절감률 계산
        Double reductionAmount = year1Total - year2Total;
        Double reductionPercentage = year1Total > 0 ?
                (reductionAmount / year1Total) * 100 : 0.0;
        Boolean isReduced = reductionAmount > 0;

        return new YearComparisonResponse(
                String.valueOf(year1),
                String.valueOf(year2),
                year1Total,
                year2Total,
                reductionAmount,
                reductionPercentage,
                isReduced
        );
    }

    /**
     * 연도별 추이 조회
     */
    public YearlyTrendResponse getYearlyTrend(Integer fromYear, Integer toYear) {
        List<YearlyTrendResponse.YearlyEmissionData> data = new ArrayList<>();

        for (int year = fromYear; year <= toYear; year++) {
            LocalDate yearStart = LocalDate.of(year, 1, 1);
            LocalDate yearEnd = LocalDate.of(year, 12, 31);
            LocalDateTime yearDateTimeStart = yearStart.atStartOfDay();
            LocalDateTime yearDateTimeEnd = yearEnd.atTime(23, 59, 59);

            Double commuteEmissions = commuteRecordRepository
                    .sumEmissionsByDateBetween(yearStart, yearEnd);
            Double businessEmissions = businessTripRepository
                    .sumEmissionsByDateBetween(yearStart, yearEnd);
            Double kakaoTEmissions = kakaoTDataRepository
                    .sumEmissionsByUsageDateBetween(yearDateTimeStart, yearDateTimeEnd);

            commuteEmissions = commuteEmissions != null ? commuteEmissions : 0.0;
            businessEmissions = businessEmissions != null ? businessEmissions : 0.0;
            kakaoTEmissions = kakaoTEmissions != null ? kakaoTEmissions : 0.0;

            Double totalEmissions = commuteEmissions + businessEmissions + kakaoTEmissions;

            data.add(new YearlyTrendResponse.YearlyEmissionData(
                    String.valueOf(year),
                    commuteEmissions,
                    businessEmissions,
                    kakaoTEmissions,
                    totalEmissions
            ));
        }

        return new YearlyTrendResponse(data);
    }

    /**
     * 탄소 배출량 감축 효과 조회
     * "만약 모든 이동을 내연기관으로 했다면" vs "실제 친환경 교통수단 사용"
     */
    public EmissionReductionResponse getEmissionReduction(LocalDate startDate, LocalDate endDate) {
        // 기본 기간 설정 (파라미터 없으면 올해)
        if (startDate == null || endDate == null) {
            int currentYear = LocalDate.now().getYear();
            startDate = LocalDate.of(currentYear, 1, 1);
            endDate = LocalDate.of(currentYear, 12, 31);
        }

        // 데이터 조회
        List<CommuteRecord> commuteRecords = commuteRecordRepository
                .findByDateBetween(startDate, endDate);

        List<BusinessTrip> businessTrips = businessTripRepository
                .findByDateBetween(startDate, endDate);

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<KakaoTData> kakaoTData = kakaoTDataRepository
                .findByUsageDateBetweenOrderByUsageDateDesc(startDateTime, endDateTime);

        // 1. 실제 배출량 계산
        Double actualEmissions = calculateActualEmissions(commuteRecords, businessTrips, kakaoTData);

        // 2. 가상 배출량 계산 (모든 이동을 내연기관으로 했을 때)
        Double hypotheticalEmissions = calculateHypotheticalEmissions(commuteRecords, businessTrips, kakaoTData);

        // 3. 감축량 및 감축률 계산
        Double reductionAmount = hypotheticalEmissions - actualEmissions;
        Double reductionPercentage = hypotheticalEmissions > 0 ?
                (reductionAmount / hypotheticalEmissions) * 100 : 0.0;

        // 4. 총 이동 거리 계산
        Double totalDistance = calculateTotalDistance(commuteRecords, businessTrips, kakaoTData);

        // 5. 친환경 교통수단 통계
        EmissionReductionResponse.EcoFriendlyStats ecoStats =
                calculateEcoFriendlyStats(commuteRecords, businessTrips, kakaoTData);

        // 6. 응답 생성
        EmissionReductionResponse response = new EmissionReductionResponse();
        response.setStartDate(startDate);
        response.setEndDate(endDate);
        response.setActualEmissions(actualEmissions);
        response.setHypotheticalEmissions(hypotheticalEmissions);
        response.setReductionAmount(reductionAmount);
        response.setReductionPercentage(reductionPercentage);
        response.setTotalDistance(totalDistance);
        response.setEcoStats(ecoStats);

        return response;
    }

    /**
     * 실제 배출량 계산
     */
    private Double calculateActualEmissions(List<CommuteRecord> commuteRecords,
                                           List<BusinessTrip> businessTrips,
                                           List<KakaoTData> kakaoTData) {
        Double commuteEmissions = commuteRecords.stream()
                .mapToDouble(r -> r.getEmissions() != null ? r.getEmissions() : 0.0)
                .sum();

        Double businessEmissions = businessTrips.stream()
                .mapToDouble(t -> t.getEmissions() != null ? t.getEmissions() : 0.0)
                .sum();

        Double kakaoTEmissions = kakaoTData.stream()
                .mapToDouble(k -> k.getEmissions() != null ? k.getEmissions() : 0.0)
                .sum();

        return commuteEmissions + businessEmissions + kakaoTEmissions;
    }

    /**
     * 가상 배출량 계산 (모든 이동을 내연기관 차량으로 했을 때)
     */
    private Double calculateHypotheticalEmissions(List<CommuteRecord> commuteRecords,
                                                  List<BusinessTrip> businessTrips,
                                                  List<KakaoTData> kakaoTData) {
        // 출퇴근: 모든 거리를 ICE로 계산
        Double commuteHypothetical = commuteRecords.stream()
                .mapToDouble(r -> {
                    Double distance = r.getDistance() != null ? r.getDistance() : 0.0;
                    // 대중교통/도보는 거리가 0이므로, 평균 거리로 가정 (10km)
                    if (distance == 0.0 && (r.getUsedCar() == null || !r.getUsedCar())) {
                        distance = 10.0; // 평균 출퇴근 거리
                    }
                    return EmissionFactors.calculateEmissions(distance, EmissionFactors.ICE_CAR);
                })
                .sum();

        // 출장: 현재 배출량 그대로 (이미 내연기관 기준)
        Double businessHypothetical = businessTrips.stream()
                .mapToDouble(t -> t.getEmissions() != null ? t.getEmissions() : 0.0)
                .sum();

        // 카카오T: 모든 거리를 일반 택시(ICE)로 계산
        Double kakaoTHypothetical = kakaoTData.stream()
                .mapToDouble(k -> {
                    Double distance = k.getDistance() != null ? k.getDistance() : 0.0;
                    return EmissionFactors.calculateEmissions(distance, EmissionFactors.ICE_CAR);
                })
                .sum();

        return commuteHypothetical + businessHypothetical + kakaoTHypothetical;
    }

    /**
     * 총 이동 거리 계산
     */
    private Double calculateTotalDistance(List<CommuteRecord> commuteRecords,
                                         List<BusinessTrip> businessTrips,
                                         List<KakaoTData> kakaoTData) {
        Double commuteDistance = commuteRecords.stream()
                .mapToDouble(r -> {
                    Double distance = r.getDistance() != null ? r.getDistance() : 0.0;
                    // 대중교통/도보는 거리 미기록이므로 평균 거리로 가정
                    if (distance == 0.0 && (r.getUsedCar() == null || !r.getUsedCar())) {
                        return 10.0;
                    }
                    return distance;
                })
                .sum();

        Double businessDistance = businessTrips.stream()
                .mapToDouble(t -> t.getDistance() != null ? t.getDistance() : 0.0)
                .sum();

        Double kakaoTDistance = kakaoTData.stream()
                .mapToDouble(k -> k.getDistance() != null ? k.getDistance() : 0.0)
                .sum();

        return commuteDistance + businessDistance + kakaoTDistance;
    }

    /**
     * 친환경 교통수단 통계 계산
     */
    private EmissionReductionResponse.EcoFriendlyStats calculateEcoFriendlyStats(
            List<CommuteRecord> commuteRecords,
            List<BusinessTrip> businessTrips,
            List<KakaoTData> kakaoTData) {

        int totalTrips = commuteRecords.size() + businessTrips.size() + kakaoTData.size();

        // 대중교통 (출퇴근에서 자가용 미사용)
        int publicTransportCount = (int) commuteRecords.stream()
                .filter(r -> r.getUsedCar() == null || !r.getUsedCar())
                .count();

        // 전기차 (출퇴근 + 카카오T)
        int evCount = (int) commuteRecords.stream()
                .filter(r -> r.getVehicleType() == VehicleType.EV)
                .count();
        evCount += (int) kakaoTData.stream()
                .filter(k -> k.getVehicleType() == VehicleType.EV)
                .count();

        // 자전거 (카카오T)
        int bikeCount = (int) kakaoTData.stream()
                .filter(k -> k.getServiceType() == KakaoTServiceType.BIKE)
                .count();

        // 도보 (출퇴근에서 거리가 0이고 자가용 미사용인 경우 일부로 가정)
        int walkCount = 0; // 정확한 판별 어려움

        // 내연기관 (출퇴근 + 출장 + 카카오T)
        int iceCount = (int) commuteRecords.stream()
                .filter(r -> r.getVehicleType() == VehicleType.ICE || r.getVehicleType() == VehicleType.HYBRID)
                .count();
        iceCount += (int) businessTrips.stream()
                .filter(t -> t.getType() != TripType.BUS) // 버스 제외
                .count();
        iceCount += (int) kakaoTData.stream()
                .filter(k -> k.getVehicleType() == VehicleType.ICE || k.getVehicleType() == VehicleType.HYBRID)
                .count();

        // 친환경 교통수단 총 횟수
        int ecoFriendlyTrips = publicTransportCount + evCount + bikeCount + walkCount;

        return new EmissionReductionResponse.EcoFriendlyStats(
                totalTrips,
                ecoFriendlyTrips,
                publicTransportCount,
                evCount,
                bikeCount,
                walkCount,
                iceCount
        );
    }

    /**
     * 포인트 요약 조회
     */
    public PointsSummaryResponse getPointsSummary(LocalDate startDate, LocalDate endDate) {
        // 출퇴근 포인트 계산
        List<CommuteRecord> commuteRecords = commuteRecordRepository
                .findByDateBetween(startDate, endDate);

        Integer commutePoints = commuteRecords.stream()
                .filter(r -> r.getPoints() != null && r.getPoints() > 0)
                .mapToInt(CommuteRecord::getPoints)
                .sum();

        Integer commutePointsCount = (int) commuteRecords.stream()
                .filter(r -> r.getPoints() != null && r.getPoints() > 0)
                .count();

        // 카카오T 포인트 계산
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<KakaoTData> kakaoTData = kakaoTDataRepository
                .findByUsageDateBetweenOrderByUsageDateDesc(startDateTime, endDateTime);

        Integer kakaoTPoints = kakaoTData.stream()
                .filter(k -> k.getPoints() != null && k.getPoints() > 0)
                .mapToInt(KakaoTData::getPoints)
                .sum();

        Integer kakaoTPointsCount = (int) kakaoTData.stream()
                .filter(k -> k.getPoints() != null && k.getPoints() > 0)
                .count();

        Integer totalPoints = commutePoints + kakaoTPoints;
        Integer pointsCount = commutePointsCount + kakaoTPointsCount;

        return new PointsSummaryResponse(
                totalPoints,
                pointsCount,
                commutePoints,
                kakaoTPoints,
                commutePointsCount,
                kakaoTPointsCount
        );
    }
}
