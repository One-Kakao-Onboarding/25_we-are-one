package com.example.kakaoonboarding.service;

import com.example.kakaoonboarding.dto.response.ExtractedData;
import com.example.kakaoonboarding.entity.VehicleType;
import com.example.kakaoonboarding.util.ReceiptDataTransformer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;

/**
 * Gemini AI API를 사용하여 영수증 이미지에서 데이터를 추출하는 서비스
 */
@Service
public class GeminiAIService {
    private static final Logger logger = LoggerFactory.getLogger(GeminiAIService.class);
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final ReceiptDataTransformer transformer;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.timeout:30000}")
    private long timeout;

    public GeminiAIService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper,
                          ReceiptDataTransformer transformer) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
        this.transformer = transformer;
    }

    /**
     * 영수증 이미지에서 데이터 추출
     */
    public ExtractedData extractReceiptData(File imageFile) {
        logger.info("Starting receipt extraction for file: {}", imageFile.getName());
        long startTime = System.currentTimeMillis();

        try {
            // 1. 이미지를 Base64로 인코딩
            String base64Image = encodeImageToBase64(imageFile);

            // 2. Gemini API 호출
            String responseJson = callGeminiAPI(base64Image);

            // 3. 응답 파싱
            ExtractedData data = parseGeminiResponse(responseJson);

            long duration = System.currentTimeMillis() - startTime;
            logger.info("Extraction successful. Duration: {}ms", duration);

            return data;

        } catch (IOException e) {
            logger.error("Failed to read image file: {}", e.getMessage());
            return createEmptyExtractedData("이미지 파일을 읽을 수 없습니다.");

        } catch (WebClientResponseException e) {
            logger.error("Gemini API error: {} - {}", e.getStatusCode(), e.getMessage());
            return createEmptyExtractedData("AI 서비스 오류: " + e.getStatusCode());

        } catch (Exception e) {
            logger.error("Unexpected error during extraction", e);
            return createEmptyExtractedData("예상치 못한 오류가 발생했습니다.");
        }
    }

    /**
     * 이미지 파일을 Base64로 인코딩
     */
    private String encodeImageToBase64(File imageFile) throws IOException {
        byte[] fileContent = Files.readAllBytes(imageFile.toPath());
        return Base64.getEncoder().encodeToString(fileContent);
    }

    /**
     * Gemini API 호출
     */
    private String callGeminiAPI(String base64Image) {
        String prompt = buildPrompt();

        // Gemini API 요청 바디 구성
        Map<String, Object> requestBody = Map.of(
            "contents", new Object[]{
                Map.of(
                    "parts", new Object[]{
                        Map.of("text", prompt),
                        Map.of(
                            "inline_data", Map.of(
                                "mime_type", "image/jpeg",
                                "data", base64Image
                            )
                        )
                    }
                )
            }
        );

        // API 호출
        logger.info("Calling Gemini API with URL: {}", apiUrl);
        String response = webClient.post()
            .uri(apiUrl)
            .header("Content-Type", "application/json")
            .header("x-goog-api-key", apiKey)
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(String.class)
            .timeout(Duration.ofMillis(timeout))
            .doOnError(error -> logger.error("Gemini API call failed", error))
            .onErrorResume(error -> {
                logger.error("Gemini API error, returning empty response", error);
                return Mono.just("{\"error\": \"API call failed\"}");
            })
            .block();

        logger.info("Gemini API response length: {} chars", response != null ? response.length() : 0);
        logger.debug("Gemini API raw response: {}", response);

        return response;
    }

    /**
     * AI 프롬프트 생성
     */
    private String buildPrompt() {
        return """
            You are a receipt analysis expert for a Korean corporate carbon tracking system.
            Analyze this transportation receipt image and extract structured data in JSON format.

            Identify the receipt type:
            - "commute": Daily commute receipts (subway, bus, taxi for work commute, personal car)
            - "business_trip": Business trip receipts (train, flight, long-distance bus)
            - "kakao_t": KakaoT app receipts (taxi, quick service, bike)
            - "unknown": If unable to determine

            Extract the following information:
            1. Receipt type and confidence (0.0-1.0)
            2. Date (YYYY-MM-DD) or DateTime (ISO-8601 if time is visible)
            3. Transportation type: subway, bus, taxi, train, flight, car, bike, quick, etc.
            4. Departure and arrival locations (if visible)
            5. Distance in km (if shown)
            6. Amount paid (if visible)
            7. Vehicle type: EV (electric), HYBRID, or ICE (gasoline/diesel) - if identifiable
            8. Route description (if shown)

            Korean keyword recognition:
            - 출발지, 도착지, 거리, 요금, 금액
            - 택시, 기차, 비행기, 버스, 지하철
            - 전기차, 하이브리드, 카카오T
            - 출퇴근, 출장, 업무

            Return ONLY valid JSON in this exact format:
            {
              "receiptType": "commute|business_trip|kakao_t|unknown",
              "confidence": 0.0-1.0,
              "extractedData": {
                "date": "YYYY-MM-DD or null",
                "dateTime": "ISO-8601 or null",
                "transportationType": "string or null",
                "departure": "string or null",
                "arrival": "string or null",
                "distance": number or null,
                "amount": number or null,
                "vehicleType": "EV|HYBRID|ICE or null",
                "route": "string or null"
              },
              "warnings": ["array of strings describing missing or ambiguous fields"]
            }

            Rules:
            - Be conservative with confidence scoring
            - Never fabricate data - use null for missing fields
            - Add warnings for uncertain or missing information
            - For Korean receipts, recognize Hangul text
            """;
    }

    /**
     * Gemini API 응답 파싱
     */
    private ExtractedData parseGeminiResponse(String responseJson) {
        try {
            JsonNode root = objectMapper.readTree(responseJson);

            // Gemini API 응답 구조: candidates[0].content.parts[0].text
            JsonNode candidates = root.get("candidates");
            if (candidates == null || candidates.isEmpty()) {
                logger.warn("No candidates in Gemini response");
                return createEmptyExtractedData("AI가 응답을 생성하지 못했습니다.");
            }

            String textContent = candidates.get(0)
                .get("content")
                .get("parts")
                .get(0)
                .get("text")
                .asText();

            // JSON 텍스트 추출 (```json 마커 제거)
            String jsonContent = extractJsonFromText(textContent);

            // AI가 반환한 JSON 파싱
            JsonNode aiResponse = objectMapper.readTree(jsonContent);
            JsonNode extractedDataNode = aiResponse.get("extractedData");

            // ExtractedData 객체 생성
            ExtractedData data = new ExtractedData();
            data.setDate(getTextValue(extractedDataNode, "date"));
            data.setTransportationType(getTextValue(extractedDataNode, "transportationType"));
            data.setDeparture(getTextValue(extractedDataNode, "departure"));
            data.setArrival(getTextValue(extractedDataNode, "arrival"));
            data.setDistance(getDoubleValue(extractedDataNode, "distance"));
            data.setAmount(getDoubleValue(extractedDataNode, "amount"));
            data.setRoute(getTextValue(extractedDataNode, "route"));

            // 차량 타입 파싱
            String vehicleTypeStr = getTextValue(extractedDataNode, "vehicleType");
            if (vehicleTypeStr != null) {
                data.setVehicleType(transformer.mapVehicleTypeFromString(vehicleTypeStr));
            }

            // DateTime 파싱
            String dateTimeStr = getTextValue(extractedDataNode, "dateTime");
            if (dateTimeStr != null) {
                try {
                    data.setDateTime(LocalDateTime.parse(dateTimeStr));
                } catch (Exception e) {
                    logger.warn("Failed to parse dateTime: {}", dateTimeStr);
                }
            }

            return data;

        } catch (Exception e) {
            logger.error("Failed to parse Gemini response", e);
            return createEmptyExtractedData("AI 응답 파싱 실패");
        }
    }

    /**
     * 텍스트에서 JSON 추출 (마크다운 코드 블록 제거)
     */
    private String extractJsonFromText(String text) {
        text = text.trim();
        if (text.startsWith("```json")) {
            text = text.substring(7);
        }
        if (text.startsWith("```")) {
            text = text.substring(3);
        }
        if (text.endsWith("```")) {
            text = text.substring(0, text.length() - 3);
        }
        return text.trim();
    }

    /**
     * JsonNode에서 텍스트 값 추출
     */
    private String getTextValue(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        if (field == null || field.isNull()) {
            return null;
        }
        return field.asText();
    }

    /**
     * JsonNode에서 Double 값 추출
     */
    private Double getDoubleValue(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        if (field == null || field.isNull()) {
            return null;
        }
        return field.asDouble();
    }

    /**
     * 빈 ExtractedData 생성 (오류 시)
     */
    private ExtractedData createEmptyExtractedData(String errorMessage) {
        ExtractedData data = new ExtractedData();
        logger.warn("Creating empty ExtractedData: {}", errorMessage);
        return data;
    }
}
