package com.example.kakaoonboarding.dto.response;

import com.example.kakaoonboarding.entity.KakaoTServiceType;
import com.example.kakaoonboarding.entity.TripType;
import com.example.kakaoonboarding.entity.VehicleType;

import java.time.LocalDateTime;

/**
 * AI가 영수증에서 추출한 데이터
 */
public class ExtractedData {
    // 메타데이터
    private String date;                    // YYYY-MM-DD 형식
    private LocalDateTime dateTime;         // ISO-8601 형식 (시간 포함)

    // 교통 정보
    private String transportationType;      // subway, bus, taxi, train, flight, car, bike, quick 등
    private String departure;               // 출발지
    private String arrival;                 // 도착지
    private String route;                   // 경로 설명

    // 재무 정보
    private Double amount;                  // 결제 금액
    private String paymentMethod;           // 결제 수단

    // 계산된 필드
    private Double distance;                // 거리 (km)
    private Double emissions;               // 탄소 배출량 (kg CO2)

    // 타입별 특화 필드
    private VehicleType vehicleType;        // EV, HYBRID, ICE
    private TripType tripType;              // TRAIN, FLIGHT, BUS
    private KakaoTServiceType kakaoTServiceType;  // TAXI, QUICK, BIKE
    private Boolean usedCar;                // 자가용 사용 여부

    public ExtractedData() {
    }

    // Getters and Setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getTransportationType() {
        return transportationType;
    }

    public void setTransportationType(String transportationType) {
        this.transportationType = transportationType;
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

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
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

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public TripType getTripType() {
        return tripType;
    }

    public void setTripType(TripType tripType) {
        this.tripType = tripType;
    }

    public KakaoTServiceType getKakaoTServiceType() {
        return kakaoTServiceType;
    }

    public void setKakaoTServiceType(KakaoTServiceType kakaoTServiceType) {
        this.kakaoTServiceType = kakaoTServiceType;
    }

    public Boolean getUsedCar() {
        return usedCar;
    }

    public void setUsedCar(Boolean usedCar) {
        this.usedCar = usedCar;
    }
}
