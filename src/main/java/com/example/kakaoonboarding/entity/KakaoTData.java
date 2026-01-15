package com.example.kakaoonboarding.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "kakao_t_data")
public class KakaoTData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String employeeId;  // 직원 ID

    @Column(nullable = false)
    private String employeeName;  // 직원 이름

    @Column(nullable = false)
    private LocalDateTime usageDate;  // 이용 날짜 및 시간

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private KakaoTServiceType serviceType;  // 서비스 유형 (taxi/quick/bike)

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;  // 차량 유형 (EV/Hybrid/ICE/Motorcycle)

    @Column(nullable = false)
    private Double distance;  // 거리 (km)

    @Column(nullable = false)
    private Double emissions;  // 탄소 배출량 (kgCO2e)

    private Integer points;  // 포인트 (친환경 차량 이용 시 지급)

    @Column(length = 500)
    private String route;  // 이동 경로

    private String department;  // 부서

    @Column(nullable = false, updatable = false)
    private LocalDateTime syncedAt;  // 동기화 시간

    @PrePersist
    protected void onCreate() {
        syncedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public LocalDateTime getUsageDate() {
        return usageDate;
    }

    public void setUsageDate(LocalDateTime usageDate) {
        this.usageDate = usageDate;
    }

    public KakaoTServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(KakaoTServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getEmissions() {
        return emissions;
    }

    public void setEmissions(Double emissions) {
        this.emissions = emissions;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalDateTime getSyncedAt() {
        return syncedAt;
    }

    public void setSyncedAt(LocalDateTime syncedAt) {
        this.syncedAt = syncedAt;
    }
}
