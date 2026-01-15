package com.example.kakaoonboarding.dto.response;

import com.example.kakaoonboarding.entity.TripType;

import java.util.List;
import java.util.Map;

public class BusinessTripHistoryResponse {
    private List<BusinessTripDto> data;
    private BusinessTripStats stats;

    public BusinessTripHistoryResponse(List<BusinessTripDto> data, BusinessTripStats stats) {
        this.data = data;
        this.stats = stats;
    }

    // Getters and Setters
    public List<BusinessTripDto> getData() {
        return data;
    }

    public void setData(List<BusinessTripDto> data) {
        this.data = data;
    }

    public BusinessTripStats getStats() {
        return stats;
    }

    public void setStats(BusinessTripStats stats) {
        this.stats = stats;
    }

    // 내부 DTO 클래스
    public static class BusinessTripDto {
        private String id;
        private String date;
        private TripType type;
        private String departure;
        private String arrival;
        private Double distance;
        private Double emissions;
        private String receipt;

        public BusinessTripDto(String id, String date, TripType type, String departure,
                             String arrival, Double distance, Double emissions, String receipt) {
            this.id = id;
            this.date = date;
            this.type = type;
            this.departure = departure;
            this.arrival = arrival;
            this.distance = distance;
            this.emissions = emissions;
            this.receipt = receipt;
        }

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public TripType getType() {
            return type;
        }

        public void setType(TripType type) {
            this.type = type;
        }

        public String getDeparture() {
            return departure;
        }

        public void setDeparture(String departure) {
            this.departure = departure;
        }

        public String getArrival() {
            return arrival;
        }

        public void setArrival(String arrival) {
            this.arrival = arrival;
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

        public String getReceipt() {
            return receipt;
        }

        public void setReceipt(String receipt) {
            this.receipt = receipt;
        }
    }

    public static class BusinessTripStats {
        private Integer totalTrips;
        private Double totalDistance;
        private Double totalEmissions;
        private Map<TripType, TypeStats> byType;

        public BusinessTripStats(Integer totalTrips, Double totalDistance,
                                Double totalEmissions, Map<TripType, TypeStats> byType) {
            this.totalTrips = totalTrips;
            this.totalDistance = totalDistance;
            this.totalEmissions = totalEmissions;
            this.byType = byType;
        }

        // Getters and Setters
        public Integer getTotalTrips() {
            return totalTrips;
        }

        public void setTotalTrips(Integer totalTrips) {
            this.totalTrips = totalTrips;
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

        public Map<TripType, TypeStats> getByType() {
            return byType;
        }

        public void setByType(Map<TripType, TypeStats> byType) {
            this.byType = byType;
        }
    }

    public static class TypeStats {
        private Long count;
        private Double emissions;

        public TypeStats(Long count, Double emissions) {
            this.count = count;
            this.emissions = emissions;
        }

        // Getters and Setters
        public Long getCount() {
            return count;
        }

        public void setCount(Long count) {
            this.count = count;
        }

        public Double getEmissions() {
            return emissions;
        }

        public void setEmissions(Double emissions) {
            this.emissions = emissions;
        }
    }
}
