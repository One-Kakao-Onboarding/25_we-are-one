package com.example.kakaoonboarding.dto.response;

import java.time.LocalDateTime;

/**
 * 카카오T 데이터 저장 응답
 */
public class KakaoTSaveResponse {
    private String id;
    private String status;
    private String message;
    private LocalDateTime createdAt;
    private Integer points;             // 획득 포인트
    private Double totalDistance;       // 이번 달 총 거리 (km)
    private Double totalEmissions;      // 이번 달 총 배출량 (kg CO2)

    public KakaoTSaveResponse() {
    }

    public KakaoTSaveResponse(String id, String status, String message,
                             LocalDateTime createdAt, Integer points,
                             Double totalDistance, Double totalEmissions) {
        this.id = id;
        this.status = status;
        this.message = message;
        this.createdAt = createdAt;
        this.points = points;
        this.totalDistance = totalDistance;
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

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public Double getTotalEmissions() {
        return totalEmissions;
    }

    public void setTotalEmissions(Double totalEmissions) {
        this.totalEmissions = totalEmissions;
    }
}
