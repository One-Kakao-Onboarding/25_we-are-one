package com.example.kakaoonboarding.dto.request;

import com.example.kakaoonboarding.entity.TripType;

public class BusinessTripRequest {
    private String date;  // ISO 날짜 형식 (YYYY-MM-DD)
    private TripType type;  // 출장 유형 (train/flight/bus)
    private String departure;  // 출발지
    private String arrival;  // 도착지
    private Double distance;  // 거리 (km)
    private Double emissions;  // 탄소 배출량 (kgCO2e)

    // 추후 인증 구현 시 사용
    private String employeeId;
    private String employeeName;
    private String department;

    // Getters and Setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
