package com.example.kakaoonboarding.dto.response;

public class EmissionsEstimateResponse {
    private Double estimatedDistance;  // 예상 거리 (km)
    private Double estimatedEmissions;  // 예상 배출량 (kgCO2e)
    private Double emissionsFactor;  // 배출 계수 (kgCO2e per km)

    public EmissionsEstimateResponse(Double estimatedDistance, Double estimatedEmissions,
                                    Double emissionsFactor) {
        this.estimatedDistance = estimatedDistance;
        this.estimatedEmissions = estimatedEmissions;
        this.emissionsFactor = emissionsFactor;
    }

    // Getters and Setters
    public Double getEstimatedDistance() {
        return estimatedDistance;
    }

    public void setEstimatedDistance(Double estimatedDistance) {
        this.estimatedDistance = estimatedDistance;
    }

    public Double getEstimatedEmissions() {
        return estimatedEmissions;
    }

    public void setEstimatedEmissions(Double estimatedEmissions) {
        this.estimatedEmissions = estimatedEmissions;
    }

    public Double getEmissionsFactor() {
        return emissionsFactor;
    }

    public void setEmissionsFactor(Double emissionsFactor) {
        this.emissionsFactor = emissionsFactor;
    }
}
