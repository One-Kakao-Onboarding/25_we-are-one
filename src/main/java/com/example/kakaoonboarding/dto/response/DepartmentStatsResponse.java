package com.example.kakaoonboarding.dto.response;

import java.util.List;

public class DepartmentStatsResponse {
    private List<DepartmentData> data;

    public DepartmentStatsResponse(List<DepartmentData> data) {
        this.data = data;
    }

    // Getters and Setters
    public List<DepartmentData> getData() {
        return data;
    }

    public void setData(List<DepartmentData> data) {
        this.data = data;
    }

    public static class DepartmentData {
        private String department;
        private Double emissions;
        private Double budget;
        private Double utilizationRate;
        private Double trend;
        private String trendDirection;

        public DepartmentData(String department, Double emissions, Double budget,
                            Double utilizationRate, Double trend, String trendDirection) {
            this.department = department;
            this.emissions = emissions;
            this.budget = budget;
            this.utilizationRate = utilizationRate;
            this.trend = trend;
            this.trendDirection = trendDirection;
        }

        // Getters and Setters
        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public Double getEmissions() {
            return emissions;
        }

        public void setEmissions(Double emissions) {
            this.emissions = emissions;
        }

        public Double getBudget() {
            return budget;
        }

        public void setBudget(Double budget) {
            this.budget = budget;
        }

        public Double getUtilizationRate() {
            return utilizationRate;
        }

        public void setUtilizationRate(Double utilizationRate) {
            this.utilizationRate = utilizationRate;
        }

        public Double getTrend() {
            return trend;
        }

        public void setTrend(Double trend) {
            this.trend = trend;
        }

        public String getTrendDirection() {
            return trendDirection;
        }

        public void setTrendDirection(String trendDirection) {
            this.trendDirection = trendDirection;
        }
    }
}
