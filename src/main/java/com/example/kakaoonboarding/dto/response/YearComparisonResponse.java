package com.example.kakaoonboarding.dto.response;

/**
 * 연도별 배출량 비교 응답 DTO
 */
public class YearComparisonResponse {
    private String baseYear;  // 기준 연도
    private String comparisonYear;  // 비교 연도
    private Double baseYearTotal;  // 기준 연도 총 배출량
    private Double comparisonYearTotal;  // 비교 연도 총 배출량
    private Double reductionAmount;  // 절감량 (kg CO2)
    private Double reductionPercentage;  // 절감률 (%)
    private Boolean isReduced;  // 감소 여부

    public YearComparisonResponse(String baseYear, String comparisonYear,
                                 Double baseYearTotal, Double comparisonYearTotal,
                                 Double reductionAmount, Double reductionPercentage,
                                 Boolean isReduced) {
        this.baseYear = baseYear;
        this.comparisonYear = comparisonYear;
        this.baseYearTotal = baseYearTotal;
        this.comparisonYearTotal = comparisonYearTotal;
        this.reductionAmount = reductionAmount;
        this.reductionPercentage = reductionPercentage;
        this.isReduced = isReduced;
    }

    // Getters and Setters
    public String getBaseYear() {
        return baseYear;
    }

    public void setBaseYear(String baseYear) {
        this.baseYear = baseYear;
    }

    public String getComparisonYear() {
        return comparisonYear;
    }

    public void setComparisonYear(String comparisonYear) {
        this.comparisonYear = comparisonYear;
    }

    public Double getBaseYearTotal() {
        return baseYearTotal;
    }

    public void setBaseYearTotal(Double baseYearTotal) {
        this.baseYearTotal = baseYearTotal;
    }

    public Double getComparisonYearTotal() {
        return comparisonYearTotal;
    }

    public void setComparisonYearTotal(Double comparisonYearTotal) {
        this.comparisonYearTotal = comparisonYearTotal;
    }

    public Double getReductionAmount() {
        return reductionAmount;
    }

    public void setReductionAmount(Double reductionAmount) {
        this.reductionAmount = reductionAmount;
    }

    public Double getReductionPercentage() {
        return reductionPercentage;
    }

    public void setReductionPercentage(Double reductionPercentage) {
        this.reductionPercentage = reductionPercentage;
    }

    public Boolean getIsReduced() {
        return isReduced;
    }

    public void setIsReduced(Boolean isReduced) {
        this.isReduced = isReduced;
    }
}
