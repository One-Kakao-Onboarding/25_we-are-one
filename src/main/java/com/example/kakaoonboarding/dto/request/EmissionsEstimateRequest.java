package com.example.kakaoonboarding.dto.request;

import com.example.kakaoonboarding.entity.TripType;

public class EmissionsEstimateRequest {
    private TripType type;  // 출장 유형 (train/flight/bus)
    private String departure;  // 출발지
    private String arrival;  // 도착지

    // Getters and Setters
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
}
