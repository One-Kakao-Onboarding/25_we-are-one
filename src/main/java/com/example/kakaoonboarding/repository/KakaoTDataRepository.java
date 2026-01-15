package com.example.kakaoonboarding.repository;

import com.example.kakaoonboarding.entity.KakaoTData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface KakaoTDataRepository extends JpaRepository<KakaoTData, Long> {

    // 날짜 범위로 조회
    List<KakaoTData> findByUsageDateBetweenOrderByUsageDateDesc(
            LocalDateTime startDate, LocalDateTime endDate);

    // 부서별 조회
    List<KakaoTData> findByDepartmentAndUsageDateBetween(
            String department, LocalDateTime startDate, LocalDateTime endDate);

    // 직원별 조회
    List<KakaoTData> findByEmployeeIdAndUsageDateBetween(
            String employeeId, LocalDateTime startDate, LocalDateTime endDate);

    // 총 배출량 조회 (날짜 범위)
    @Query("SELECT COALESCE(SUM(k.emissions), 0.0) FROM KakaoTData k " +
           "WHERE k.usageDate BETWEEN :startDate AND :endDate")
    Double sumEmissionsByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // 총 배출량 조회 (날짜 범위) - alias
    @Query("SELECT COALESCE(SUM(k.emissions), 0.0) FROM KakaoTData k " +
           "WHERE k.usageDate BETWEEN :startDate AND :endDate")
    Double sumEmissionsByUsageDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // 부서별 배출량 조회
    @Query("SELECT COALESCE(SUM(k.emissions), 0.0) FROM KakaoTData k " +
           "WHERE k.department = :department AND k.usageDate BETWEEN :startDate AND :endDate")
    Double sumEmissionsByDepartmentAndDateBetween(
            String department, LocalDateTime startDate, LocalDateTime endDate);
}
