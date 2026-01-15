package com.example.kakaoonboarding.repository;

import com.example.kakaoonboarding.entity.BusinessTrip;
import com.example.kakaoonboarding.entity.TripType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BusinessTripRepository extends JpaRepository<BusinessTrip, Long> {

    // 특정 직원의 출장 기록 조회 (날짜 범위)
    List<BusinessTrip> findByEmployeeIdAndDateBetweenOrderByDateDesc(
            String employeeId, LocalDate startDate, LocalDate endDate);

    // 특정 직원의 출장 기록 조회 (최근순)
    List<BusinessTrip> findByEmployeeIdOrderByDateDesc(String employeeId);

    // 특정 직원의 출장 기록 조회 (출장 유형별)
    List<BusinessTrip> findByEmployeeIdAndType(String employeeId, TripType type);

    // 특정 직원의 총 출장 거리 (특정 기간)
    @Query("SELECT COALESCE(SUM(b.distance), 0.0) FROM BusinessTrip b " +
           "WHERE b.employeeId = :employeeId AND b.date BETWEEN :startDate AND :endDate")
    Double sumDistanceByEmployeeIdAndDateBetween(
            String employeeId, LocalDate startDate, LocalDate endDate);

    // 특정 직원의 총 탄소 배출량 (특정 기간)
    @Query("SELECT COALESCE(SUM(b.emissions), 0.0) FROM BusinessTrip b " +
           "WHERE b.employeeId = :employeeId AND b.date BETWEEN :startDate AND :endDate")
    Double sumEmissionsByEmployeeIdAndDateBetween(
            String employeeId, LocalDate startDate, LocalDate endDate);

    // 전체 직원의 출장 기록 조회 (날짜 범위)
    List<BusinessTrip> findByDateBetween(LocalDate startDate, LocalDate endDate);

    // 전체 직원의 총 탄소 배출량 (특정 기간)
    @Query("SELECT COALESCE(SUM(b.emissions), 0.0) FROM BusinessTrip b " +
           "WHERE b.date BETWEEN :startDate AND :endDate")
    Double sumEmissionsByDateBetween(LocalDate startDate, LocalDate endDate);

    // 부서별 출장 기록 조회
    List<BusinessTrip> findByDepartmentAndDateBetween(
            String department, LocalDate startDate, LocalDate endDate);

    // 출장 유형별 통계
    @Query("SELECT b.type, COUNT(b), COALESCE(SUM(b.emissions), 0.0) FROM BusinessTrip b " +
           "WHERE b.employeeId = :employeeId AND b.date BETWEEN :startDate AND :endDate " +
           "GROUP BY b.type")
    List<Object[]> getStatsByType(String employeeId, LocalDate startDate, LocalDate endDate);
}
