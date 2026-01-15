package com.example.kakaoonboarding.dto.response;

import java.time.LocalDateTime;

public class BusinessTripResponse {
    private String id;
    private String status;
    private String message;
    private LocalDateTime createdAt;
    private Integer totalTripsThisMonth;  // 이번 달 총 출장 횟수
    private Double totalEmissionsThisMonth;  // 이번 달 총 배출량

    public BusinessTripResponse(String id, String status, String message,
                               LocalDateTime createdAt, Integer totalTripsThisMonth,
                               Double totalEmissionsThisMonth) {
        this.id = id;
        this.status = status;
        this.message = message;
        this.createdAt = createdAt;
        this.totalTripsThisMonth = totalTripsThisMonth;
        this.totalEmissionsThisMonth = totalEmissionsThisMonth;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getTotalTripsThisMonth() {
        return totalTripsThisMonth;
    }

    public void setTotalTripsThisMonth(Integer totalTripsThisMonth) {
        this.totalTripsThisMonth = totalTripsThisMonth;
    }

    public Double getTotalEmissionsThisMonth() {
        return totalEmissionsThisMonth;
    }

    public void setTotalEmissionsThisMonth(Double totalEmissionsThisMonth) {
        this.totalEmissionsThisMonth = totalEmissionsThisMonth;
    }
}
