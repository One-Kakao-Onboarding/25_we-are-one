package com.example.kakaoonboarding.dto.response;

import java.time.LocalDate;

/**
 * 탄소 배출량 감축 효과 응답
 * "만약 모든 이동을 내연기관으로 했다면" vs "실제 친환경 교통수단 사용"
 */
public class EmissionReductionResponse {
    private LocalDate startDate;
    private LocalDate endDate;

    // 실제 배출량
    private Double actualEmissions;          // 실제 배출량 (kg CO2)

    // 가상 배출량 (모든 이동을 내연기관으로 했을 때)
    private Double hypotheticalEmissions;    // 가상 배출량 (kg CO2)

    // 감축 효과
    private Double reductionAmount;          // 감축량 (kg CO2)
    private Double reductionPercentage;      // 감축률 (%)

    // 친환경 교통수단 통계
    private EcoFriendlyStats ecoStats;

    // 총 이동 거리
    private Double totalDistance;            // 총 거리 (km)

    public EmissionReductionResponse() {
    }

    public static class EcoFriendlyStats {
        private Integer totalTrips;           // 총 이동 횟수
        private Integer ecoFriendlyTrips;     // 친환경 교통수단 이용 횟수
        private Double ecoFriendlyRatio;      // 친환경 이용률 (%)

        // 교통수단별 세부 통계
        private Integer publicTransportCount; // 대중교통 (지하철, 버스)
        private Integer evCount;              // 전기차
        private Integer bikeCount;            // 자전거
        private Integer walkCount;            // 도보
        private Integer iceCount;             // 내연기관

        public EcoFriendlyStats() {
        }

        public EcoFriendlyStats(Integer totalTrips, Integer ecoFriendlyTrips,
                               Integer publicTransportCount, Integer evCount,
                               Integer bikeCount, Integer walkCount, Integer iceCount) {
            this.totalTrips = totalTrips;
            this.ecoFriendlyTrips = ecoFriendlyTrips;
            this.ecoFriendlyRatio = totalTrips > 0 ?
                (ecoFriendlyTrips * 100.0 / totalTrips) : 0.0;
            this.publicTransportCount = publicTransportCount;
            this.evCount = evCount;
            this.bikeCount = bikeCount;
            this.walkCount = walkCount;
            this.iceCount = iceCount;
        }

        // Getters and Setters
        public Integer getTotalTrips() {
            return totalTrips;
        }

        public void setTotalTrips(Integer totalTrips) {
            this.totalTrips = totalTrips;
        }

        public Integer getEcoFriendlyTrips() {
            return ecoFriendlyTrips;
        }

        public void setEcoFriendlyTrips(Integer ecoFriendlyTrips) {
            this.ecoFriendlyTrips = ecoFriendlyTrips;
        }

        public Double getEcoFriendlyRatio() {
            return ecoFriendlyRatio;
        }

        public void setEcoFriendlyRatio(Double ecoFriendlyRatio) {
            this.ecoFriendlyRatio = ecoFriendlyRatio;
        }

        public Integer getPublicTransportCount() {
            return publicTransportCount;
        }

        public void setPublicTransportCount(Integer publicTransportCount) {
            this.publicTransportCount = publicTransportCount;
        }

        public Integer getEvCount() {
            return evCount;
        }

        public void setEvCount(Integer evCount) {
            this.evCount = evCount;
        }

        public Integer getBikeCount() {
            return bikeCount;
        }

        public void setBikeCount(Integer bikeCount) {
            this.bikeCount = bikeCount;
        }

        public Integer getWalkCount() {
            return walkCount;
        }

        public void setWalkCount(Integer walkCount) {
            this.walkCount = walkCount;
        }

        public Integer getIceCount() {
            return iceCount;
        }

        public void setIceCount(Integer iceCount) {
            this.iceCount = iceCount;
        }
    }

    // Getters and Setters
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getActualEmissions() {
        return actualEmissions;
    }

    public void setActualEmissions(Double actualEmissions) {
        this.actualEmissions = actualEmissions;
    }

    public Double getHypotheticalEmissions() {
        return hypotheticalEmissions;
    }

    public void setHypotheticalEmissions(Double hypotheticalEmissions) {
        this.hypotheticalEmissions = hypotheticalEmissions;
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

    public EcoFriendlyStats getEcoStats() {
        return ecoStats;
    }

    public void setEcoStats(EcoFriendlyStats ecoStats) {
        this.ecoStats = ecoStats;
    }

    public Double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Double totalDistance) {
        this.totalDistance = totalDistance;
    }
}
