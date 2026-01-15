package com.example.kakaoonboarding.service;

import com.example.kakaoonboarding.config.EmissionFactors;
import com.example.kakaoonboarding.config.SessionUser;
import com.example.kakaoonboarding.dto.request.BusinessTripRequest;
import com.example.kakaoonboarding.dto.request.CommuteCheckInRequest;
import com.example.kakaoonboarding.dto.request.ReceiptConfirmRequest;
import com.example.kakaoonboarding.dto.response.*;
import com.example.kakaoonboarding.entity.KakaoTData;
import com.example.kakaoonboarding.util.ReceiptDataTransformer;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 영수증 처리 워크플로우를 오케스트레이션하는 서비스
 */
@Service
public class ReceiptProcessingService {
    private static final Logger logger = LoggerFactory.getLogger(ReceiptProcessingService.class);

    private final GeminiAIService geminiAIService;
    private final CommuteService commuteService;
    private final BusinessTripService businessTripService;
    private final KakaoTDataService kakaoTDataService;
    private final AuthService authService;
    private final ReceiptDataTransformer transformer;

    @Value("${receipt.temp.directory}")
    private String tempDirectory;

    @Value("${receipt.ai.confidence-threshold:0.6}")
    private double confidenceThreshold;

    public ReceiptProcessingService(GeminiAIService geminiAIService,
                                   CommuteService commuteService,
                                   BusinessTripService businessTripService,
                                   KakaoTDataService kakaoTDataService,
                                   AuthService authService,
                                   ReceiptDataTransformer transformer) {
        this.geminiAIService = geminiAIService;
        this.commuteService = commuteService;
        this.businessTripService = businessTripService;
        this.kakaoTDataService = kakaoTDataService;
        this.authService = authService;
        this.transformer = transformer;
    }

    /**
     * 영수증 분석 (Step 1: 업로드 & AI 처리)
     */
    public ReceiptAnalysisResponse analyzeReceipt(MultipartFile file, HttpSession session) {
        SessionUser user = authService.getCurrentUser(session);
        String employeeId = user.getEmployeeId();

        try {
            // 1. 임시 파일 저장
            String tempFilePath = saveToTempDirectory(file, employeeId);

            // 2. Gemini AI로 데이터 추출
            File imageFile = new File(tempFilePath);
            ExtractedData extractedData = geminiAIService.extractReceiptData(imageFile);

            // 3. 데이터 보완 (거리 계산, 배출량 계산)
            ExtractedData enhancedData = enhanceData(extractedData);

            // 4. 타입 감지 및 신뢰도 계산
            String detectedType = detectReceiptType(enhancedData);
            double confidence = calculateConfidence(enhancedData, detectedType);

            // 5. 응답 생성
            ReceiptAnalysisResponse response = ReceiptAnalysisResponse.success(
                    detectedType,
                    confidence,
                    enhancedData,
                    tempFilePath
            );

            // 6. 경고 메시지 추가
            addWarnings(response, enhancedData, confidence);

            logger.info("Receipt analysis completed for employee: {}, type: {}, confidence: {}",
                    employeeId, detectedType, confidence);

            return response;

        } catch (IOException e) {
            logger.error("Failed to save temp file for employee: {}", employeeId, e);
            return ReceiptAnalysisResponse.failed("파일 저장 실패: " + e.getMessage());

        } catch (Exception e) {
            logger.error("Unexpected error during receipt analysis for employee: {}", employeeId, e);
            return ReceiptAnalysisResponse.failed("영수증 분석 중 오류가 발생했습니다.");
        }
    }

    /**
     * 영수증 확인 및 저장 (Step 2: 사용자 확인 후 저장)
     */
    public Object processConfirmedReceipt(ReceiptConfirmRequest request, HttpSession session) {
        try {
            Object response = null;

            // 타입에 따라 적절한 서비스로 라우팅
            switch (request.getReceiptType()) {
                case "COMMUTE":
                    CommuteCheckInRequest commuteRequest = toCommuteRequest(request);
                    response = commuteService.checkIn(commuteRequest, session);
                    break;

                case "BUSINESS_TRIP":
                    BusinessTripRequest tripRequest = toBusinessTripRequest(request);
                    response = businessTripService.register(tripRequest, null);
                    break;

                case "KAKAO_T":
                    SessionUser user = authService.getCurrentUser(session);
                    KakaoTData kakaoTData = toKakaoTData(request, user);
                    response = kakaoTDataService.save(kakaoTData, session);
                    break;

                default:
                    throw new IllegalArgumentException("알 수 없는 영수증 타입: " + request.getReceiptType());
            }

            // 임시 파일 삭제
            deleteTempFile(request.getTempFilePath());

            logger.info("Receipt confirmed and saved: type={}, tempFile={}",
                    request.getReceiptType(), request.getTempFilePath());

            return response;

        } catch (Exception e) {
            logger.error("Failed to process confirmed receipt", e);
            throw new RuntimeException("영수증 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 데이터 보완 (거리 계산, 배출량 계산)
     */
    private ExtractedData enhanceData(ExtractedData data) {
        // 거리가 없으면 기본값 설정 (실제로는 KakaoMobilityService 사용 가능)
        if (data.getDistance() == null) {
            data.setDistance(0.0);
        }

        // 배출량 계산
        if (data.getEmissions() == null && data.getDistance() != null && data.getDistance() > 0) {
            double emissionFactor = determineEmissionFactor(data);
            data.setEmissions(EmissionFactors.calculateEmissions(data.getDistance(), emissionFactor));
        } else if (data.getEmissions() == null) {
            data.setEmissions(0.0);
        }

        return data;
    }

    /**
     * 영수증 타입 자동 감지
     */
    private String detectReceiptType(ExtractedData data) {
        String transportType = data.getTransportationType();
        if (transportType == null) {
            return "UNKNOWN";
        }

        String type = transportType.toLowerCase();

        // 카카오T 키워드
        if (type.contains("kakao") || type.contains("카카오")) {
            return "KAKAO_T";
        }

        // 출장 키워드
        if (type.contains("flight") || type.contains("train") || type.contains("비행기") || type.contains("기차")) {
            return "BUSINESS_TRIP";
        }

        // 장거리 이동 (출장 가능성)
        if (data.getDistance() != null && data.getDistance() > 100) {
            return "BUSINESS_TRIP";
        }

        // 기본값: 출퇴근
        return "COMMUTE";
    }

    /**
     * 신뢰도 계산
     */
    private double calculateConfidence(ExtractedData data, String detectedType) {
        double confidence = 0.5; // 기본 신뢰도

        // 주요 필드가 있으면 신뢰도 증가
        if (data.getDate() != null) confidence += 0.15;
        if (data.getTransportationType() != null) confidence += 0.2;
        if (data.getDistance() != null) confidence += 0.1;
        if (data.getAmount() != null) confidence += 0.05;

        // 타입별 추가 검증
        if (detectedType.equals("BUSINESS_TRIP")) {
            if (data.getDeparture() != null && data.getArrival() != null) {
                confidence += 0.1;
            }
        }

        return Math.min(1.0, confidence);
    }

    /**
     * 경고 메시지 추가
     */
    private void addWarnings(ReceiptAnalysisResponse response, ExtractedData data, double confidence) {
        if (confidence < confidenceThreshold) {
            response.addWarning("낮은 신뢰도: 추출된 데이터를 확인해주세요.");
        }

        if (data.getDate() == null) {
            response.addWarning("날짜 정보가 누락되었습니다.");
        }

        if (data.getTransportationType() == null) {
            response.addWarning("교통수단 정보가 불명확합니다.");
        }

        if (data.getDistance() == null || data.getDistance() == 0.0) {
            response.addWarning("거리 정보가 없습니다.");
        }
    }

    /**
     * 배출 계수 결정
     */
    private double determineEmissionFactor(ExtractedData data) {
        String transportType = data.getTransportationType();
        if (transportType == null) {
            return 0.0;
        }

        String type = transportType.toLowerCase();
        if (type.contains("flight") || type.contains("비행기")) {
            return EmissionFactors.FLIGHT;
        } else if (type.contains("train") || type.contains("기차")) {
            return EmissionFactors.TRAIN;
        } else if (type.contains("car") || type.contains("taxi") || type.contains("자가용")) {
            return EmissionFactors.TAXI;
        }

        return 0.0; // 친환경 교통수단
    }

    /**
     * ReceiptConfirmRequest → CommuteCheckInRequest 변환
     */
    private CommuteCheckInRequest toCommuteRequest(ReceiptConfirmRequest request) {
        CommuteCheckInRequest commuteRequest = new CommuteCheckInRequest();
        commuteRequest.setDate(request.getDate());
        commuteRequest.setUsedCar(request.getUsedCar() != null ? request.getUsedCar() : false);
        commuteRequest.setVehicleType(request.getVehicleType());
        commuteRequest.setDistance(request.getDistance() != null ? request.getDistance() : 0.0);
        commuteRequest.setEmissions(request.getEmissions() != null ? request.getEmissions() : 0.0);
        return commuteRequest;
    }

    /**
     * ReceiptConfirmRequest → BusinessTripRequest 변환
     */
    private BusinessTripRequest toBusinessTripRequest(ReceiptConfirmRequest request) {
        BusinessTripRequest tripRequest = new BusinessTripRequest();
        tripRequest.setDate(request.getDate());
        tripRequest.setType(request.getTripType());
        tripRequest.setDeparture(request.getDeparture());
        tripRequest.setArrival(request.getArrival());
        tripRequest.setDistance(request.getDistance() != null ? request.getDistance() : 0.0);
        tripRequest.setEmissions(request.getEmissions() != null ? request.getEmissions() : 0.0);
        return tripRequest;
    }

    /**
     * ReceiptConfirmRequest → KakaoTData 변환
     */
    private KakaoTData toKakaoTData(ReceiptConfirmRequest request, SessionUser user) {
        KakaoTData data = new KakaoTData();
        data.setEmployeeId(user.getEmployeeId());
        data.setEmployeeName(user.getName());
        data.setDepartment(user.getDepartment());
        data.setUsageDate(request.getUsageDateTime() != null ?
                request.getUsageDateTime() :
                java.time.LocalDate.parse(request.getDate()).atStartOfDay());
        data.setServiceType(request.getKakaoTServiceType());
        data.setVehicleType(request.getVehicleType());
        data.setDistance(request.getDistance() != null ? request.getDistance() : 0.0);
        data.setEmissions(request.getEmissions() != null ? request.getEmissions() : 0.0);
        data.setRoute(request.getRoute());
        return data;
    }

    /**
     * 임시 디렉토리에 파일 저장
     */
    private String saveToTempDirectory(MultipartFile file, String employeeId) throws IOException {
        // 임시 디렉토리 생성
        Path tempDir = Paths.get(tempDirectory);
        if (!Files.exists(tempDir)) {
            Files.createDirectories(tempDir);
        }

        // 파일 확장자 추출
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 고유 파일명 생성
        String filename = employeeId + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID() + extension;
        Path filePath = tempDir.resolve(filename);

        // 파일 저장
        file.transferTo(filePath.toFile());

        return filePath.toString();
    }

    /**
     * 임시 파일 삭제
     */
    private void deleteTempFile(String tempFilePath) {
        if (tempFilePath == null || tempFilePath.isEmpty()) {
            return;
        }

        try {
            Path path = Paths.get(tempFilePath);
            Files.deleteIfExists(path);
            logger.info("Deleted temp file: {}", tempFilePath);
        } catch (IOException e) {
            logger.warn("Failed to delete temp file: {}", tempFilePath, e);
        }
    }
}
