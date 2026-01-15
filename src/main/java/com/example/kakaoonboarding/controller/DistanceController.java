package com.example.kakaoonboarding.controller;

import com.example.kakaoonboarding.dto.Coordinates;
import com.example.kakaoonboarding.dto.DistanceRequest;
import com.example.kakaoonboarding.dto.DistanceResponse;
import com.example.kakaoonboarding.dto.kakao.KakaoMobilityResponse;
import com.example.kakaoonboarding.service.KakaoLocalService;
import com.example.kakaoonboarding.service.KakaoMobilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/distance")
public class DistanceController {

    private final KakaoLocalService kakaoLocalService;
    private final KakaoMobilityService kakaoMobilityService;

    public DistanceController(KakaoLocalService kakaoLocalService,
                             KakaoMobilityService kakaoMobilityService) {
        this.kakaoLocalService = kakaoLocalService;
        this.kakaoMobilityService = kakaoMobilityService;
    }

    @PostMapping
    public ResponseEntity<DistanceResponse> calculateDistance(@RequestBody DistanceRequest request) {
        try {
            System.out.println("\n========== 거리 계산 요청 시작 ==========");
            System.out.println("출발지: " + request.getOrigin());
            System.out.println("목적지: " + request.getDestination());

            // 1. 출발지와 목적지의 좌표를 가져옴
            System.out.println("\n[1단계] 출발지 좌표 검색");
            Coordinates originCoords = kakaoLocalService.getCoordinates(request.getOrigin());

            System.out.println("\n[2단계] 목적지 좌표 검색");
            Coordinates destCoords = kakaoLocalService.getCoordinates(request.getDestination());

            // 2. 두 좌표 사이의 거리를 계산
            System.out.println("\n[3단계] 거리 계산");
            KakaoMobilityResponse.Summary summary = kakaoMobilityService.getDistance(originCoords, destCoords);

            // 3. 응답 생성
            System.out.println("\n[4단계] 응답 생성 완료");
            DistanceResponse response = new DistanceResponse(
                    request.getOrigin(),
                    request.getDestination(),
                    originCoords,
                    destCoords,
                    summary.getDistance(),
                    summary.getDuration()
            );

            System.out.println("========== 거리 계산 성공! ==========\n");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("\n========== 오류 발생 ==========");
            e.printStackTrace();
            System.out.println("================================\n");
            return ResponseEntity.badRequest().body(null);
        }
    }
}
