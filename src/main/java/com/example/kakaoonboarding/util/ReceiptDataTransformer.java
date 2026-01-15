package com.example.kakaoonboarding.util;

import com.example.kakaoonboarding.config.SessionUser;
import com.example.kakaoonboarding.dto.request.BusinessTripRequest;
import com.example.kakaoonboarding.dto.request.CommuteCheckInRequest;
import com.example.kakaoonboarding.dto.response.ExtractedData;
import com.example.kakaoonboarding.entity.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * AI 추출 데이터를 기존 DTO/Entity로 변환하는 유틸리티
 */
@Component
public class ReceiptDataTransformer {

    /**
     * 출퇴근 데이터로 변환
     */
    public CommuteCheckInRequest toCommuteRequest(ExtractedData data) {
        CommuteCheckInRequest request = new CommuteCheckInRequest();
        request.setDate(data.getDate());

        // 교통수단 타입 판별
        String transportType = data.getTransportationType();
        boolean usedCar = isCarTransportation(transportType);

        request.setUsedCar(usedCar);

        if (usedCar) {
            // 자가용인 경우 차량 타입 설정
            request.setVehicleType(mapVehicleType(data.getVehicleType()));
            request.setDistance(data.getDistance() != null ? data.getDistance() : 0.0);
        } else {
            // 대중교통/자전거/도보인 경우
            request.setVehicleType(null);
            request.setDistance(0.0);
        }

        request.setEmissions(data.getEmissions() != null ? data.getEmissions() : 0.0);

        return request;
    }

    /**
     * 출장 데이터로 변환
     */
    public BusinessTripRequest toBusinessTripRequest(ExtractedData data) {
        BusinessTripRequest request = new BusinessTripRequest();
        request.setDate(data.getDate());
        request.setDeparture(data.getDeparture());
        request.setArrival(data.getArrival());
        request.setDistance(data.getDistance() != null ? data.getDistance() : 0.0);

        // 교통수단 타입을 TripType으로 매핑
        request.setType(mapToTripType(data.getTransportationType()));

        // 배출량 설정
        request.setEmissions(data.getEmissions() != null ? data.getEmissions() : 0.0);

        return request;
    }

    /**
     * 카카오T 데이터로 변환
     */
    public KakaoTData toKakaoTData(ExtractedData data, SessionUser user) {
        KakaoTData kakaoTData = new KakaoTData();
        kakaoTData.setEmployeeId(user.getEmployeeId());
        kakaoTData.setEmployeeName(user.getName());
        kakaoTData.setDepartment(user.getDepartment());

        // 사용 일시 설정 (dateTime이 있으면 사용, 없으면 date 사용)
        if (data.getDateTime() != null) {
            kakaoTData.setUsageDate(data.getDateTime());
        } else if (data.getDate() != null) {
            kakaoTData.setUsageDate(LocalDate.parse(data.getDate()).atStartOfDay());
        }

        // 서비스 타입 설정
        kakaoTData.setServiceType(mapToKakaoTServiceType(data.getTransportationType()));

        // 차량 타입 설정
        kakaoTData.setVehicleType(mapVehicleType(data.getVehicleType()));

        kakaoTData.setDistance(data.getDistance() != null ? data.getDistance() : 0.0);
        kakaoTData.setEmissions(data.getEmissions() != null ? data.getEmissions() : 0.0);

        // 경로 설정
        if (data.getRoute() != null) {
            kakaoTData.setRoute(data.getRoute());
        } else if (data.getDeparture() != null && data.getArrival() != null) {
            kakaoTData.setRoute(data.getDeparture() + " → " + data.getArrival());
        }

        return kakaoTData;
    }

    /**
     * 자가용 교통수단인지 판별
     */
    private boolean isCarTransportation(String transportationType) {
        if (transportationType == null) return false;
        String type = transportationType.toLowerCase();
        return type.equals("car") || type.equals("자가용") || type.equals("automobile");
    }

    /**
     * 차량 타입 매핑
     */
    private VehicleType mapVehicleType(VehicleType vehicleType) {
        // 이미 VehicleType enum이면 그대로 반환
        if (vehicleType != null) {
            return vehicleType;
        }
        // 기본값: 내연기관
        return VehicleType.ICE;
    }

    /**
     * 문자열을 VehicleType으로 매핑
     */
    public VehicleType mapVehicleTypeFromString(String vehicleTypeStr) {
        if (vehicleTypeStr == null) return VehicleType.ICE;

        String type = vehicleTypeStr.toUpperCase();
        return switch (type) {
            case "EV", "ELECTRIC", "전기차" -> VehicleType.EV;
            case "HYBRID", "하이브리드" -> VehicleType.HYBRID;
            default -> VehicleType.ICE;
        };
    }

    /**
     * 교통수단을 TripType으로 매핑
     */
    private TripType mapToTripType(String transportationType) {
        if (transportationType == null) return TripType.BUS;

        String type = transportationType.toLowerCase();
        return switch (type) {
            case "flight", "airplane", "plane", "항공", "비행기" -> TripType.FLIGHT;
            case "train", "ktx", "srt", "기차", "열차" -> TripType.TRAIN;
            default -> TripType.BUS;
        };
    }

    /**
     * 교통수단을 KakaoTServiceType으로 매핑
     */
    private KakaoTServiceType mapToKakaoTServiceType(String transportationType) {
        if (transportationType == null) return KakaoTServiceType.TAXI;

        String type = transportationType.toLowerCase();
        return switch (type) {
            case "bike", "bicycle", "자전거", "따릉이" -> KakaoTServiceType.BIKE;
            case "quick", "motorcycle", "오토바이", "퀵" -> KakaoTServiceType.QUICK;
            default -> KakaoTServiceType.TAXI;
        };
    }
}
