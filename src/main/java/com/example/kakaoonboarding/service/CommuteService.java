package com.example.kakaoonboarding.service;

import com.example.kakaoonboarding.config.SessionUser;
import com.example.kakaoonboarding.dto.request.CommuteCheckInRequest;
import com.example.kakaoonboarding.dto.response.CommuteCheckInResponse;
import com.example.kakaoonboarding.dto.response.CommuteHistoryResponse;
import com.example.kakaoonboarding.dto.response.CommuteStatisticsResponse;
import com.example.kakaoonboarding.entity.CommuteRecord;
import com.example.kakaoonboarding.repository.CommuteRecordRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommuteService {

    private final CommuteRecordRepository commuteRecordRepository;
    private final AuthService authService;

    public CommuteService(CommuteRecordRepository commuteRecordRepository,
                         AuthService authService) {
        this.commuteRecordRepository = commuteRecordRepository;
        this.authService = authService;
    }

    /**
     * 출근 정보 등록
     */
    public CommuteCheckInResponse checkIn(CommuteCheckInRequest request, HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        SessionUser user = authService.getCurrentUser(session);

        String employeeId = user.getEmployeeId();
        String employeeName = user.getName();
        String department = user.getDepartment();

        // 엔티티 생성 및 저장
        CommuteRecord record = new CommuteRecord();
        record.setEmployeeId(employeeId);
        record.setEmployeeName(employeeName);
        record.setDepartment(department);
        record.setDate(LocalDate.parse(request.getDate()));
        record.setUsedCar(request.getUsedCar());
        record.setVehicleType(request.getVehicleType());
        record.setDistance(request.getDistance());
        record.setEmissions(request.getEmissions());

        CommuteRecord saved = commuteRecordRepository.save(record);

        // 이번 달 총 배출량 계산
        YearMonth currentMonth = YearMonth.now();
        LocalDate startOfMonth = currentMonth.atDay(1);
        LocalDate endOfMonth = currentMonth.atEndOfMonth();

        Double totalEmissions = commuteRecordRepository
                .sumEmissionsByEmployeeIdAndDateBetween(employeeId, startOfMonth, endOfMonth);

        return new CommuteCheckInResponse(
                "C-" + saved.getId(),
                "success",
                "출근 정보가 등록되었습니다",
                saved.getCreatedAt(),
                totalEmissions
        );
    }

    /**
     * 출근 기록 조회
     */
    @Transactional(readOnly = true)
    public CommuteHistoryResponse getHistory(LocalDate startDate, LocalDate endDate, Integer limit, HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        SessionUser user = authService.getCurrentUser(session);
        String employeeId = user.getEmployeeId();

        // 날짜 범위 설정 (기본값: 이번 달)
        if (startDate == null || endDate == null) {
            YearMonth currentMonth = YearMonth.now();
            startDate = currentMonth.atDay(1);
            endDate = currentMonth.atEndOfMonth();
        }

        // 기록 조회
        List<CommuteRecord> records = commuteRecordRepository
                .findByEmployeeIdAndDateBetweenOrderByDateDesc(employeeId, startDate, endDate);

        // limit 적용
        if (limit != null && limit > 0 && records.size() > limit) {
            records = records.subList(0, limit);
        }

        // DTO 변환
        List<CommuteHistoryResponse.CommuteRecordDto> data = records.stream()
                .map(r -> new CommuteHistoryResponse.CommuteRecordDto(
                        r.getDate().toString(),
                        r.getUsedCar(),
                        r.getVehicleType(),
                        r.getDistance(),
                        r.getEmissions()
                ))
                .collect(Collectors.toList());

        // 통계 계산
        Long carDays = commuteRecordRepository.countCarDaysByEmployeeIdAndDateBetween(
                employeeId, startDate, endDate);
        Long totalDays = (long) records.size();
        Double totalEmissions = commuteRecordRepository.sumEmissionsByEmployeeIdAndDateBetween(
                employeeId, startDate, endDate);
        Double carPercentage = totalDays > 0 ? (carDays * 100.0 / totalDays) : 0.0;

        CommuteHistoryResponse.CommuteStats stats = new CommuteHistoryResponse.CommuteStats(
                carDays, totalDays, totalEmissions, carPercentage
        );

        return new CommuteHistoryResponse(data, stats);
    }

    /**
     * 출근 통계 조회
     */
    @Transactional(readOnly = true)
    public CommuteStatisticsResponse getStatistics(String period, HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        SessionUser user = authService.getCurrentUser(session);
        String employeeId = user.getEmployeeId();

        LocalDate startDate, endDate;
        LocalDate prevStartDate, prevEndDate;

        // 기간 설정
        if ("year".equals(period)) {
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

        // 통계 계산
        Long carDays = commuteRecordRepository.countCarDaysByEmployeeIdAndDateBetween(
                employeeId, startDate, endDate);

        List<CommuteRecord> records = commuteRecordRepository
                .findByEmployeeIdAndDateBetweenOrderByDateDesc(employeeId, startDate, endDate);
        Long totalDays = (long) records.size();

        Double totalEmissions = commuteRecordRepository.sumEmissionsByEmployeeIdAndDateBetween(
                employeeId, startDate, endDate);

        Double carPercentage = totalDays > 0 ? (carDays * 100.0 / totalDays) : 0.0;
        Double avgEmissions = totalDays > 0 ? (totalEmissions / totalDays) : 0.0;

        // 전월 대비 변화율 계산
        Double prevTotalEmissions = commuteRecordRepository.sumEmissionsByEmployeeIdAndDateBetween(
                employeeId, prevStartDate, prevEndDate);
        Double trend = 0.0;
        if (prevTotalEmissions > 0) {
            trend = ((totalEmissions - prevTotalEmissions) / prevTotalEmissions) * 100;
        }

        return new CommuteStatisticsResponse(
                carDays, totalDays, carPercentage, totalEmissions, avgEmissions, trend
        );
    }
}
