package com.example.kakaoonboarding.dto.response;

import java.util.ArrayList;
import java.util.List;

/**
 * 영수증 AI 분석 결과 응답
 */
public class ReceiptAnalysisResponse {
    private String status;              // "success" or "failed"
    private String message;             // 사용자에게 표시할 메시지
    private String detectedType;        // "COMMUTE", "BUSINESS_TRIP", "KAKAO_T", "UNKNOWN"
    private Double confidence;          // AI 신뢰도 점수 (0.0-1.0)
    private String tempFilePath;        // 임시 파일 경로 (나중에 삭제용)
    private ExtractedData extractedData; // 추출된 데이터
    private List<String> warnings;      // 경고 메시지 (누락되거나 불명확한 필드)

    public ReceiptAnalysisResponse() {
        this.warnings = new ArrayList<>();
    }

    public static ReceiptAnalysisResponse success(String detectedType, Double confidence,
                                                   ExtractedData data, String tempFilePath) {
        ReceiptAnalysisResponse response = new ReceiptAnalysisResponse();
        response.setStatus("success");
        response.setMessage("영수증 분석이 완료되었습니다.");
        response.setDetectedType(detectedType);
        response.setConfidence(confidence);
        response.setExtractedData(data);
        response.setTempFilePath(tempFilePath);
        return response;
    }

    public static ReceiptAnalysisResponse failed(String message) {
        ReceiptAnalysisResponse response = new ReceiptAnalysisResponse();
        response.setStatus("failed");
        response.setMessage(message);
        response.setDetectedType("UNKNOWN");
        response.setConfidence(0.0);
        response.setExtractedData(new ExtractedData());
        response.addWarning("AI 처리 실패: 수동으로 입력해주세요.");
        return response;
    }

    public void addWarning(String warning) {
        this.warnings.add(warning);
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetectedType() {
        return detectedType;
    }

    public void setDetectedType(String detectedType) {
        this.detectedType = detectedType;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public String getTempFilePath() {
        return tempFilePath;
    }

    public void setTempFilePath(String tempFilePath) {
        this.tempFilePath = tempFilePath;
    }

    public ExtractedData getExtractedData() {
        return extractedData;
    }

    public void setExtractedData(ExtractedData extractedData) {
        this.extractedData = extractedData;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }
}
