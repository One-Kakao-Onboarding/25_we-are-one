package com.example.kakaoonboarding.dto.response;

import java.util.List;

public class MonthlyTrendResponse {
    private List<MonthlyData> data;

    public MonthlyTrendResponse(List<MonthlyData> data) {
        this.data = data;
    }

    // Getters and Setters
    public List<MonthlyData> getData() {
        return data;
    }

    public void setData(List<MonthlyData> data) {
        this.data = data;
    }

    public static class MonthlyData {
        private String month;
        private Double commute;
        private Double business;
        private Double logistics;
        private Double total;

        public MonthlyData(String month, Double commute, Double business,
                          Double logistics, Double total) {
            this.month = month;
            this.commute = commute;
            this.business = business;
            this.logistics = logistics;
            this.total = total;
        }

        // Getters and Setters
        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public Double getCommute() {
            return commute;
        }

        public void setCommute(Double commute) {
            this.commute = commute;
        }

        public Double getBusiness() {
            return business;
        }

        public void setBusiness(Double business) {
            this.business = business;
        }

        public Double getLogistics() {
            return logistics;
        }

        public void setLogistics(Double logistics) {
            this.logistics = logistics;
        }

        public Double getTotal() {
            return total;
        }

        public void setTotal(Double total) {
            this.total = total;
        }
    }
}
