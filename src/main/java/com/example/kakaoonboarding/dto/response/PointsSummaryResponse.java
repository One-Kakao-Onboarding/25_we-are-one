package com.example.kakaoonboarding.dto.response;

/**
 * 포인트 요약 응답 DTO
 */
public class PointsSummaryResponse {
    private Integer totalPoints;  // 총 포인트
    private Integer pointsCount;  // 포인트 지급 건수
    private Integer commutePoints;  // 출퇴근 포인트
    private Integer kakaoTPoints;  // 카카오T 포인트
    private Integer commutePointsCount;  // 출퇴근 포인트 건수
    private Integer kakaoTPointsCount;  // 카카오T 포인트 건수

    public PointsSummaryResponse(Integer totalPoints, Integer pointsCount,
                                Integer commutePoints, Integer kakaoTPoints,
                                Integer commutePointsCount, Integer kakaoTPointsCount) {
        this.totalPoints = totalPoints;
        this.pointsCount = pointsCount;
        this.commutePoints = commutePoints;
        this.kakaoTPoints = kakaoTPoints;
        this.commutePointsCount = commutePointsCount;
        this.kakaoTPointsCount = kakaoTPointsCount;
    }

    // Getters and Setters
    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Integer getPointsCount() {
        return pointsCount;
    }

    public void setPointsCount(Integer pointsCount) {
        this.pointsCount = pointsCount;
    }

    public Integer getCommutePoints() {
        return commutePoints;
    }

    public void setCommutePoints(Integer commutePoints) {
        this.commutePoints = commutePoints;
    }

    public Integer getKakaoTPoints() {
        return kakaoTPoints;
    }

    public void setKakaoTPoints(Integer kakaoTPoints) {
        this.kakaoTPoints = kakaoTPoints;
    }

    public Integer getCommutePointsCount() {
        return commutePointsCount;
    }

    public void setCommutePointsCount(Integer commutePointsCount) {
        this.commutePointsCount = commutePointsCount;
    }

    public Integer getKakaoTPointsCount() {
        return kakaoTPointsCount;
    }

    public void setKakaoTPointsCount(Integer kakaoTPointsCount) {
        this.kakaoTPointsCount = kakaoTPointsCount;
    }
}
