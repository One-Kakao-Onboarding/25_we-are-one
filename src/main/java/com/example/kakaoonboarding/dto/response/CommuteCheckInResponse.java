package com.example.kakaoonboarding.dto.response;

import java.time.LocalDateTime;

public class CommuteCheckInResponse {
    private String id;
    private String status;
    private String message;
    private LocalDateTime createdAt;
    private Double totalEmissions;  // 이번 달 총 배출량

    public CommuteCheckInResponse(String id, String status, String message,
                                 LocalDateTime createdAt, Double totalEmissions) {
        this.id = id;
        this.status = status;
        this.message = message;
        this.createdAt = createdAt;
        this.totalEmissions = totalEmissions;
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

    public Double getTotalEmissions() {
        return totalEmissions;
    }

    public void setTotalEmissions(Double totalEmissions) {
        this.totalEmissions = totalEmissions;
    }
}
