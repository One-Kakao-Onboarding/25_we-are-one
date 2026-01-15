package com.example.kakaoonboarding.dto.response;

import java.util.List;

/**
 * 연도별 추이 응답 DTO
 */
public class YearlyTrendResponse {
    private List<YearlyEmissionData> data;

    public YearlyTrendResponse(List<YearlyEmissionData> data) {
        this.data = data;
    }

    // Getters and Setters
    public List<YearlyEmissionData> getData() {
        return data;
    }

    public void setData(List<YearlyEmissionData> data) {
        this.data = data;
    }

    /**
     * 연도별 배출량 데이터
     */
    public static class YearlyEmissionData {
        private String year;  // 연도
        private Double commuteEmissions;  // 출퇴근 배출량
        private Double businessTripEmissions;  // 출장 배출량
        private Double kakaoTEmissions;  // 카카오T 배출량
        private Double totalEmissions;  // 총 배출량
        private Double target;  // 목표 배출량 (optional)

        public YearlyEmissionData(String year, Double commuteEmissions, Double businessTripEmissions,
                                 Double kakaoTEmissions, Double totalEmissions) {
            this.year = year;
            this.commuteEmissions = commuteEmissions;
            this.businessTripEmissions = businessTripEmissions;
            this.kakaoTEmissions = kakaoTEmissions;
            this.totalEmissions = totalEmissions;
        }

        public YearlyEmissionData(String year, Double commuteEmissions, Double businessTripEmissions,
                                 Double kakaoTEmissions, Double totalEmissions, Double target) {
            this.year = year;
            this.commuteEmissions = commuteEmissions;
            this.businessTripEmissions = businessTripEmissions;
            this.kakaoTEmissions = kakaoTEmissions;
            this.totalEmissions = totalEmissions;
            this.target = target;
        }

        // Getters and Setters
        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public Double getCommuteEmissions() {
            return commuteEmissions;
        }

        public void setCommuteEmissions(Double commuteEmissions) {
            this.commuteEmissions = commuteEmissions;
        }

        public Double getBusinessTripEmissions() {
            return businessTripEmissions;
        }

        public void setBusinessTripEmissions(Double businessTripEmissions) {
            this.businessTripEmissions = businessTripEmissions;
        }

        public Double getKakaoTEmissions() {
            return kakaoTEmissions;
        }

        public void setKakaoTEmissions(Double kakaoTEmissions) {
            this.kakaoTEmissions = kakaoTEmissions;
        }

        public Double getTotalEmissions() {
            return totalEmissions;
        }

        public void setTotalEmissions(Double totalEmissions) {
            this.totalEmissions = totalEmissions;
        }

        public Double getTarget() {
            return target;
        }

        public void setTarget(Double target) {
            this.target = target;
        }
    }
}
