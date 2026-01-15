package com.example.kakaoonboarding.dto.response;

import com.example.kakaoonboarding.entity.VehicleType;

import java.util.List;

public class CommuteHistoryResponse {
    private List<CommuteRecordDto> data;
    private CommuteStats stats;

    public CommuteHistoryResponse(List<CommuteRecordDto> data, CommuteStats stats) {
        this.data = data;
        this.stats = stats;
    }

    // Getters and Setters
    public List<CommuteRecordDto> getData() {
        return data;
    }

    public void setData(List<CommuteRecordDto> data) {
        this.data = data;
    }

    public CommuteStats getStats() {
        return stats;
    }

    public void setStats(CommuteStats stats) {
        this.stats = stats;
    }

    // 내부 DTO 클래스
    public static class CommuteRecordDto {
        private String date;
        private Boolean usedCar;
        private VehicleType vehicleType;
        private Double distance;
        private Double emissions;

        public CommuteRecordDto(String date, Boolean usedCar, VehicleType vehicleType,
                               Double distance, Double emissions) {
            this.date = date;
            this.usedCar = usedCar;
            this.vehicleType = vehicleType;
            this.distance = distance;
            this.emissions = emissions;
        }

        // Getters and Setters
        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Boolean getUsedCar() {
            return usedCar;
        }

        public void setUsedCar(Boolean usedCar) {
            this.usedCar = usedCar;
        }

        public VehicleType getVehicleType() {
            return vehicleType;
        }

        public void setVehicleType(VehicleType vehicleType) {
            this.vehicleType = vehicleType;
        }

        public Double getDistance() {
            return distance;
        }

        public void setDistance(Double distance) {
            this.distance = distance;
        }

        public Double getEmissions() {
            return emissions;
        }

        public void setEmissions(Double emissions) {
            this.emissions = emissions;
        }
    }

    public static class CommuteStats {
        private Long carDays;  // 자가용 이용 일수
        private Long totalDays;  // 총 출근 일수
        private Double totalEmissions;  // 총 배출량
        private Double carPercentage;  // 자가용 이용 비율

        public CommuteStats(Long carDays, Long totalDays, Double totalEmissions, Double carPercentage) {
            this.carDays = carDays;
            this.totalDays = totalDays;
            this.totalEmissions = totalEmissions;
            this.carPercentage = carPercentage;
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

        public Double getTotalEmissions() {
            return totalEmissions;
        }

        public void setTotalEmissions(Double totalEmissions) {
            this.totalEmissions = totalEmissions;
        }

        public Double getCarPercentage() {
            return carPercentage;
        }

        public void setCarPercentage(Double carPercentage) {
            this.carPercentage = carPercentage;
        }
    }
}
