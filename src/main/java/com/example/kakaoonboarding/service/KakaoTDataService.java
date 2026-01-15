package com.example.kakaoonboarding.service;

import com.example.kakaoonboarding.config.SessionUser;
import com.example.kakaoonboarding.dto.response.KakaoTSaveResponse;
import com.example.kakaoonboarding.entity.KakaoTData;
import com.example.kakaoonboarding.repository.KakaoTDataRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 카카오T 데이터 처리 서비스
 */
@Service
public class KakaoTDataService {

    private final KakaoTDataRepository kakaoTDataRepository;
    private final PointsService pointsService;
    private final AuthService authService;

    public KakaoTDataService(KakaoTDataRepository kakaoTDataRepository,
                            PointsService pointsService,
                            AuthService authService) {
        this.kakaoTDataRepository = kakaoTDataRepository;
        this.pointsService = pointsService;
        this.authService = authService;
    }

    /**
     * 카카오T 데이터 저장
     */
    @Transactional
    public KakaoTSaveResponse save(KakaoTData data, HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        SessionUser user = authService.getCurrentUser(session);

        // 사용자 정보 설정 (데이터에 없는 경우)
        if (data.getEmployeeId() == null) {
            data.setEmployeeId(user.getEmployeeId());
        }
        if (data.getEmployeeName() == null) {
            data.setEmployeeName(user.getName());
        }
        if (data.getDepartment() == null) {
            data.setDepartment(user.getDepartment());
        }

        // 포인트 계산 (배출량 기반)
        Integer points = pointsService.calculateKakaoTPointsByEmissions(data.getEmissions());
        data.setPoints(points);

        // 데이터베이스에 저장
        KakaoTData savedData = kakaoTDataRepository.save(data);

        // 이번 달 통계 조회
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfMonth = now.withDayOfMonth(now.toLocalDate().lengthOfMonth())
                                      .withHour(23).withMinute(59).withSecond(59);

        // 이번 달 총 거리 계산
        Double totalDistance = kakaoTDataRepository
                .findByEmployeeIdAndUsageDateBetween(
                        user.getEmployeeId(), startOfMonth, endOfMonth)
                .stream()
                .filter(k -> k.getDistance() != null)
                .mapToDouble(KakaoTData::getDistance)
                .sum();

        // 이번 달 총 배출량 조회
        Double totalEmissions = kakaoTDataRepository
                .findByEmployeeIdAndUsageDateBetween(
                        user.getEmployeeId(), startOfMonth, endOfMonth)
                .stream()
                .filter(k -> k.getEmissions() != null)
                .mapToDouble(KakaoTData::getEmissions)
                .sum();

        // 응답 생성
        return new KakaoTSaveResponse(
                "KT-" + savedData.getId(),
                "success",
                "카카오T 데이터가 성공적으로 등록되었습니다.",
                savedData.getSyncedAt(),
                points,
                totalDistance,
                totalEmissions
        );
    }
}
