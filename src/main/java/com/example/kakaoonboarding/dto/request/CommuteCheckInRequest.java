package com.example.kakaoonboarding.dto.request;

import com.example.kakaoonboarding.entity.VehicleType;

public class CommuteCheckInRequest {
    private String date;  // ISO 날짜 형식 (YYYY-MM-DD)
    private Boolean usedCar;  // 자가용 이용 여부
    private VehicleType vehicleType;  // 차량 유형 (EV/Hybrid/ICE)
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
