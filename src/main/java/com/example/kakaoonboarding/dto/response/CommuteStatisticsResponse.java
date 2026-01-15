package com.example.kakaoonboarding.dto.response;

public class CommuteStatisticsResponse {
    private Long carDays;  // 자가용 이용 일수
    private Long totalDays;  // 총 출근 일수
    private Double carPercentage;  // 자가용 이용 비율
    private Double totalEmissions;  // 총 배출량
    private Double avgEmissions;  // 평균 배출량
    private Double trend;  // 전월 대비 변화율 (%)

    public CommuteStatisticsResponse(Long carDays, Long totalDays, Double carPercentage,
                                    Double totalEmissions, Double avgEmissions, Double trend) {
        this.carDays = carDays;
        this.totalDays = totalDays;
        this.carPercentage = carPercentage;
        this.totalEmissions = totalEmissions;
        this.avgEmissions = avgEmissions;
        this.trend = trend;
    }

    // Getters and Setters
    public Long getCarDays() {
        return carDays;
    }

    public void setCarDays(Long carDays) {
        this.carDays = carDays;
    }

    public Long getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(Long totalDays) {
        this.totalDays = totalDays;
    }

    public Double getCarPercentage() {
        return carPercentage;
    }

    public void setCarPercentage(Double carPercentage) {
        this.carPercentage = carPercentage;
    }

    public Double getTotalEmissions() {
        return totalEmissions;
    }

    public void setTotalEmissions(Double totalEmissions) {
        this.totalEmissions = totalEmissions;
    }

    public Double getAvgEmissions() {
        return avgEmissions;
    }

    public void setAvgEmissions(Double avgEmissions) {
        this.avgEmissions = avgEmissions;
    }

    public Double getTrend() {
        return trend;
    }

    public void setTrend(Double trend) {
        this.trend = trend;
    }
}
