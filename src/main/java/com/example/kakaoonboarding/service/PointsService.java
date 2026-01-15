package com.example.kakaoonboarding.service;

import com.example.kakaoonboarding.entity.VehicleType;
import org.springframework.stereotype.Service;

/**
 * 포인트 계산 서비스
 */
@Service
public class PointsService {

    private static final int ECO_FRIENDLY_POINTS = 10;  // 친환경 교통수단 이용 시 지급 포인트

    /**
     * 출퇴근 포인트 계산
     * - 자가용을 이용하지 않은 경우: 10 포인트 (대중교통, 자전거, 도보 등)
     * - 전기차를 이용한 경우: 10 포인트
     * - 내연기관/하이브리드 이용: 0 포인트
     */
    public Integer calculateCommutePoints(Boolean usedCar, VehicleType vehicleType) {
        // 자가용을 이용하지 않은 경우 (대중교통, 자전거, 도보 등)
        if (!usedCar) {
            return ECO_FRIENDLY_POINTS;
        }

        // 전기차를 이용한 경우
        if (vehicleType == VehicleType.EV) {
            return ECO_FRIENDLY_POINTS;
        }

        // 내연기관 또는 하이브리드 이용
        return 0;
    }

    /**
     * 카카오T 서비스 포인트 계산
     * - 전기차(EV) 이용: 10 포인트
     * - 자전거(BIKE) 이용: 10 포인트
     * - 그 외: 0 포인트
     */
    public Integer calculateKakaoTPoints(VehicleType vehicleType) {
        if (vehicleType == VehicleType.EV) {
            return ECO_FRIENDLY_POINTS;
        }

        // 자전거는 VehicleType이 아니라 ServiceType으로 판단해야 하므로
        // 배출량이 0인 경우로 판단
        return 0;
    }

    /**
     * 배출량 기반 포인트 계산 (카카오T용)
     * - 배출량이 0인 경우: 10 포인트 (전기차, 자전거 등)
     * - 그 외: 0 포인트
     */
    public Integer calculateKakaoTPointsByEmissions(Double emissions) {
        if (emissions != null && emissions == 0.0) {
            return ECO_FRIENDLY_POINTS;
        }
        return 0;
    }
}
