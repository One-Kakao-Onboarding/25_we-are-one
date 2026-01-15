package com.example.kakaoonboarding.repository;

import com.example.kakaoonboarding.entity.CommuteRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CommuteRecordRepository extends JpaRepository<CommuteRecord, Long> {

    // 특정 직원의 출근 기록 조회 (날짜 범위)
    List<CommuteRecord> findByEmployeeIdAndDateBetweenOrderByDateDesc(
            String employeeId, LocalDate startDate, LocalDate endDate);

    // 특정 직원의 출근 기록 조회 (최근순)
    List<CommuteRecord> findByEmployeeIdOrderByDateDesc(String employeeId);

    // 특정 직원의 자가용 이용 일수 (특정 기간)
    @Query("SELECT COUNT(c) FROM CommuteRecord c WHERE c.employeeId = :employeeId " +
           "AND c.usedCar = true AND c.date BETWEEN :startDate AND :endDate")
    Long countCarDaysByEmployeeIdAndDateBetween(
            String employeeId, LocalDate startDate, LocalDate endDate);

    // 특정 직원의 총 탄소 배출량 (특정 기간)
    @Query("SELECT COALESCE(SUM(c.emissions), 0.0) FROM CommuteRecord c " +
           "WHERE c.employeeId = :employeeId AND c.date BETWEEN :startDate AND :endDate")
    Double sumEmissionsByEmployeeIdAndDateBetween(
            String employeeId, LocalDate startDate, LocalDate endDate);

    // 전체 직원의 출근 기록 조회 (날짜 범위)
    List<CommuteRecord> findByDateBetween(LocalDate startDate, LocalDate endDate);

    // 부서별 출근 기록 조회
    List<CommuteRecord> findByDepartmentAndDateBetween(
            String department, LocalDate startDate, LocalDate endDate);
}
