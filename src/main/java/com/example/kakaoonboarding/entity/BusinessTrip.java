package com.example.kakaoonboarding.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "business_trips")
public class BusinessTrip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String employeeId;  // 직원 ID

    @Column(nullable = false)
    private String employeeName;  // 직원 이름

    @Column(nullable = false)
    private LocalDate date;  // 출장 날짜

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TripType type;  // 출장 수단 (기차/항공/버스)

    @Column(nullable = false)
    private String departure;  // 출발지

    @Column(nullable = false)
    private String arrival;  // 도착지

    @Column(nullable = false)
    private Double distance;  // 거리 (km)

    @Column(nullable = false)
    private Double emissions;  // 탄소 배출량 (kgCO2e)

    private String receiptFileName;  // 영수증 파일명 (선택)

    private String receiptFilePath;  // 영수증 파일 경로 (선택)

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

    public TripType getType() {
        return type;
    }

    public void setType(TripType type) {
        this.type = type;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
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

    public String getReceiptFileName() {
        return receiptFileName;
    }

    public void setReceiptFileName(String receiptFileName) {
        this.receiptFileName = receiptFileName;
    }

    public String getReceiptFilePath() {
        return receiptFilePath;
    }

    public void setReceiptFilePath(String receiptFilePath) {
        this.receiptFilePath = receiptFilePath;
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
