package com.example.kakaoonboarding.dto.response;

import java.util.Map;

public class DashboardOverviewResponse {
    private Summary summary;
    private Map<String, Double> vehicleEmissions;
    private Map<String, Double> flightEmissions;
    private Map<String, Double> logisticsEmissions;

    public DashboardOverviewResponse(Summary summary, Map<String, Double> vehicleEmissions,
                                    Map<String, Double> flightEmissions,
                                    Map<String, Double> logisticsEmissions) {
        this.summary = summary;
        this.vehicleEmissions = vehicleEmissions;
        this.flightEmissions = flightEmissions;
        this.logisticsEmissions = logisticsEmissions;
    }

    // Getters and Setters
    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public Map<String, Double> getVehicleEmissions() {
        return vehicleEmissions;
    }

    public void setVehicleEmissions(Map<String, Double> vehicleEmissions) {
        this.vehicleEmissions = vehicleEmissions;
    }

    public Map<String, Double> getFlightEmissions() {
        return flightEmissions;
    }

    public void setFlightEmissions(Map<String, Double> flightEmissions) {
        this.flightEmissions = flightEmissions;
    }

    public Map<String, Double> getLogisticsEmissions() {
        return logisticsEmissions;
    }

    public void setLogisticsEmissions(Map<String, Double> logisticsEmissions) {
        this.logisticsEmissions = logisticsEmissions;
    }

    public static class Summary {
        private Double totalEmissions;
        private Double previousMonthEmissions;
        private Double trend;
        private Double evRatio;
        private Integer participantCount;
        private Integer totalEmployees;

        public Summary(Double totalEmissions, Double previousMonthEmissions, Double trend,
                      Double evRatio, Integer participantCount, Integer totalEmployees) {
            this.totalEmissions = totalEmissions;
            this.previousMonthEmissions = previousMonthEmissions;
            this.trend = trend;
            this.evRatio = evRatio;
            this.participantCount = participantCount;
            this.totalEmployees = totalEmployees;
        }

        // Getters and Setters
        public Double getTotalEmissions() {
            return totalEmissions;
        }

        public void setTotalEmissions(Double totalEmissions) {
            this.totalEmissions = totalEmissions;
        }

        public Double getPreviousMonthEmissions() {
            return previousMonthEmissions;
        }

        public void setPreviousMonthEmissions(Double previousMonthEmissions) {
            this.previousMonthEmissions = previousMonthEmissions;
        }

        public Double getTrend() {
            return trend;
        }

        public void setTrend(Double trend) {
            this.trend = trend;
        }

        public Double getEvRatio() {
            return evRatio;
        }

        public void setEvRatio(Double evRatio) {
            this.evRatio = evRatio;
        }

        public Integer getParticipantCount() {
            return participantCount;
        }

        public void setParticipantCount(Integer participantCount) {
            this.participantCount = participantCount;
        }

        public Integer getTotalEmployees() {
            return totalEmployees;
        }

        public void setTotalEmployees(Integer totalEmployees) {
            this.totalEmployees = totalEmployees;
        }
    }
}
