package com.example.kakaoonboarding.controller;

import com.example.kakaoonboarding.dto.request.ReceiptConfirmRequest;
import com.example.kakaoonboarding.dto.response.ReceiptAnalysisResponse;
import com.example.kakaoonboarding.service.ReceiptProcessingService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 영수증 업로드 및 AI 분석 API
 */
@RestController
@RequestMapping("/api/receipt")
public class ReceiptController {
    private static final Logger logger = LoggerFactory.getLogger(ReceiptController.class);

    private final ReceiptProcessingService receiptProcessingService;

    @Value("${receipt.upload.max-size:10485760}")
    private long maxFileSize;

    @Value("${receipt.upload.allowed-types}")
    private String allowedTypes;

    public ReceiptController(ReceiptProcessingService receiptProcessingService) {
        this.receiptProcessingService = receiptProcessingService;
    }

    /**
     * 영수증 업로드 및 AI 분석
     * POST /api/receipt/upload
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadReceipt(
            @RequestPart("receipt") MultipartFile receipt,
            HttpSession session) {

        logger.info("Receipt upload request received, file: {}", receipt.getOriginalFilename());

        try {
            // 1. 파일 검증
            validateFile(receipt);

            // 2. 영수증 분석
            ReceiptAnalysisResponse response = receiptProcessingService.analyzeReceipt(receipt, session);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.warn("Invalid file upload: {}", e.getMessage());
            return ResponseEntity.badRequest().body(
                    ReceiptAnalysisResponse.failed("파일 검증 실패: " + e.getMessage())
            );

        } catch (Exception e) {
            logger.error("Unexpected error during receipt upload", e);
            return ResponseEntity.internalServerError().body(
                    ReceiptAnalysisResponse.failed("서버 오류가 발생했습니다.")
            );
        }
    }

    /**
     * 영수증 확인 및 저장
     * POST /api/receipt/confirm
     */
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmReceipt(
            @RequestBody ReceiptConfirmRequest request,
            HttpSession session) {

        logger.info("Receipt confirmation request received, type: {}", request.getReceiptType());

        try {
            // 영수증 타입 검증
            if (request.getReceiptType() == null || request.getReceiptType().isEmpty()) {
                return ResponseEntity.badRequest().body(
                        Map.of("status", "failed", "message", "영수증 타입이 필요합니다.")
                );
            }

            // 날짜 검증
            if (request.getDate() == null || request.getDate().isEmpty()) {
                return ResponseEntity.badRequest().body(
                        Map.of("status", "failed", "message", "날짜가 필요합니다.")
                );
            }

            // 처리 및 저장
            Object response = receiptProcessingService.processConfirmedReceipt(request, session);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.warn("Invalid confirm request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(
                    Map.of("status", "failed", "message", e.getMessage())
            );

        } catch (Exception e) {
            logger.error("Unexpected error during receipt confirmation", e);
            return ResponseEntity.internalServerError().body(
                    Map.of("status", "failed", "message", "서버 오류가 발생했습니다.")
            );
        }
    }

    /**
     * 파일 검증
     */
    private void validateFile(MultipartFile file) {
        // 1. 파일이 비어있는지 확인
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }

        // 2. 파일 크기 확인
        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException(
                    String.format("파일 크기가 너무 큽니다. 최대 크기: %d MB", maxFileSize / 1024 / 1024)
            );
        }

        // 3. 파일 타입 확인
        String contentType = file.getContentType();
        if (contentType == null) {
            throw new IllegalArgumentException("파일 타입을 확인할 수 없습니다.");
        }

        List<String> allowedTypesList = Arrays.asList(allowedTypes.split(","));
        if (!allowedTypesList.contains(contentType)) {
            throw new IllegalArgumentException(
                    "지원하지 않는 파일 형식입니다. 허용되는 형식: " + allowedTypes
            );
        }

        // 4. 이미지 무결성 확인
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new IllegalArgumentException("손상된 이미지 파일입니다.");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("이미지 파일을 읽을 수 없습니다: " + e.getMessage());
        }

        logger.info("File validation passed: {}", file.getOriginalFilename());
    }
}
