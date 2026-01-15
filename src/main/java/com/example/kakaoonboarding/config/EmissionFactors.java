package com.example.kakaoonboarding.config;

import com.example.kakaoonboarding.entity.KakaoTServiceType;
import com.example.kakaoonboarding.entity.TripType;
import com.example.kakaoonboarding.entity.VehicleType;

/**
 * 교통수단별 탄소 배출 계수 (kgCO2e per km)
 */
public class EmissionFactors {

    // 출장 교통수단
    public static final double FLIGHT = 0.14253;      // 비행기
    public static final double TRAIN = 0.03546;       // 기차
    public static final double BUS = 0.0;             // 대중교통 (버스)

    // 자가용 차량 유형
    public static final double ICE_CAR = 0.17304;     // 내연기관 자가용
    public static final double HYBRID_CAR = 0.17304;  // 하이브리드 (일단 내연기관과 동일)
    public static final double EV_CAR = 0.0;          // 전기차

    // 카카오T 서비스
    public static final double TAXI = 0.17304;        // 택시
    public static final double QUICK = 0.17304;       // 퀵서비스 (오토바이/차량)
    public static final double BIKE = 0.0;            // 자전거

    /**
     * 출장 교통수단별 배출 계수 조회
     */
    public static double getFactorByTripType(TripType type) {
        return switch (type) {
            case FLIGHT -> FLIGHT;
            case TRAIN -> TRAIN;
            case BUS -> BUS;
        };
    }

    /**
     * 자가용 차량 유형별 배출 계수 조회
     */
    public static double getFactorByVehicleType(VehicleType type) {
        if (type == null) {
            return 0.0;
        }
        return switch (type) {
            case ICE -> ICE_CAR;
            case HYBRID -> HYBRID_CAR;
            case EV -> EV_CAR;
        };
    }

    /**
     * 카카오T 서비스 유형별 배출 계수 조회
     */
    public static double getFactorByKakaoTServiceType(KakaoTServiceType type) {
        return switch (type) {
            case TAXI -> TAXI;
            case QUICK -> QUICK;
            case BIKE -> BIKE;
        };
    }

    /**
     * 배출량 계산 (거리 * 배출 계수)
     */
    public static double calculateEmissions(double distanceInKm, double factor) {
        return distanceInKm * factor;
    }
}
