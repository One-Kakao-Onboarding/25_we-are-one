package com.example.kakaoonboarding.dto.request;

import com.example.kakaoonboarding.entity.KakaoTServiceType;
import com.example.kakaoonboarding.entity.TripType;
import com.example.kakaoonboarding.entity.VehicleType;

import java.time.LocalDateTime;

/**
 * 영수증 확인 요청 (사용자가 수정한 데이터 포함)
 * 모든 타입(출퇴근/출장/카카오T)의 필드를 포함하는 Union Type
 */
public class ReceiptConfirmRequest {
    // 필수: 영수증 타입
    private String receiptType;         // "COMMUTE", "BUSINESS_TRIP", "KAKAO_T"

    // 임시 파일 경로 (삭제용)
    private String tempFilePath;

    // 공통 필드
    private String date;                // YYYY-MM-DD
    private Double distance;            // km
    private Double emissions;           // kg CO2

    // 출퇴근 필드
    private Boolean usedCar;            // 자가용 사용 여부
    private VehicleType vehicleType;    // EV, HYBRID, ICE

    // 출장 필드
    private TripType tripType;          // TRAIN, FLIGHT, BUS
    private String departure;           // 출발지
    private String arrival;             // 도착지

    // 카카오T 필드
    private KakaoTServiceType kakaoTServiceType; // TAXI, QUICK, BIKE
    private LocalDateTime usageDateTime;         // 사용 일시 (시간 포함)
    private String route;                        // 경로

    public ReceiptConfirmRequest() {
    }

    // Getters and Setters
    public String getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
    }

    public String getTempFilePath() {
        return tempFilePath;
    }

    public void setTempFilePath(String tempFilePath) {
        this.tempFilePath = tempFilePath;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public TripType getTripType() {
        return tripType;
    }

    public void setTripType(TripType tripType) {
        this.tripType = tripType;
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

    public KakaoTServiceType getKakaoTServiceType() {
        return kakaoTServiceType;
    }

    public void setKakaoTServiceType(KakaoTServiceType kakaoTServiceType) {
        this.kakaoTServiceType = kakaoTServiceType;
    }

    public LocalDateTime getUsageDateTime() {
        return usageDateTime;
    }

    public void setUsageDateTime(LocalDateTime usageDateTime) {
        this.usageDateTime = usageDateTime;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }
}
