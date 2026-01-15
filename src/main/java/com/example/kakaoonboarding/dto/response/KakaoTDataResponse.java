package com.example.kakaoonboarding.dto.response;

import com.example.kakaoonboarding.entity.KakaoTServiceType;
import com.example.kakaoonboarding.entity.VehicleType;

import java.util.List;

public class KakaoTDataResponse {
    private List<KakaoTDataDto> data;
    private Pagination pagination;

    public KakaoTDataResponse(List<KakaoTDataDto> data, Pagination pagination) {
        this.data = data;
        this.pagination = pagination;
    }

    // Getters and Setters
    public List<KakaoTDataDto> getData() {
        return data;
    }

    public void setData(List<KakaoTDataDto> data) {
        this.data = data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public static class KakaoTDataDto {
        private String id;
        private String date;
        private KakaoTServiceType type;
        private VehicleType vehicleType;
        private Double distance;
        private Double emissions;
        private String route;
        private String department;

        public KakaoTDataDto(String id, String date, KakaoTServiceType type,
                           VehicleType vehicleType, Double distance, Double emissions,
                           String route, String department) {
            this.id = id;
            this.date = date;
            this.type = type;
            this.vehicleType = vehicleType;
            this.distance = distance;
            this.emissions = emissions;
            this.route = route;
            this.department = department;
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

        public KakaoTServiceType getType() {
            return type;
        }

        public void setType(KakaoTServiceType type) {
            this.type = type;
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

        public String getRoute() {
            return route;
        }

        public void setRoute(String route) {
            this.route = route;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }
    }

    public static class Pagination {
        private Long total;
        private Integer limit;
        private Integer offset;

        public Pagination(Long total, Integer limit, Integer offset) {
            this.total = total;
            this.limit = limit;
            this.offset = offset;
        }

        // Getters and Setters
        public Long getTotal() {
            return total;
        }

        public void setTotal(Long total) {
            this.total = total;
        }

        public Integer getLimit() {
            return limit;
        }

        public void setLimit(Integer limit) {
            this.limit = limit;
        }

        public Integer getOffset() {
            return offset;
        }

        public void setOffset(Integer offset) {
            this.offset = offset;
        }
    }
}
