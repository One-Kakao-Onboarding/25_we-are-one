package com.example.kakaoonboarding.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "commute_records")
public class CommuteRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String employeeId;  // 직원 ID (추후 User 엔티티와 연결)

    @Column(nullable = false)
    private String employeeName;  // 직원 이름

    @Column(nullable = false)
    private LocalDate date;  // 출근 날짜

    @Column(nullable = false)
    private Boolean usedCar;  // 자가용 이용 여부

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;  // 차량 유형 (자가용 이용시)

    private Double distance;  // 거리 (km)

    private Double emissions;  // 탄소 배출량 (kgCO2e)

    private String department;  // 부서

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;  // 생성 시간

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean getUsedCar() {
        return usedCar;
    }

    public void setUsedCar(Boolean usedCar) {
        this.usedCar = usedCar;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
