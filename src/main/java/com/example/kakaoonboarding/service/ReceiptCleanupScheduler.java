package com.example.kakaoonboarding.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.stream.Stream;

/**
 * 오래된 영수증 임시 파일을 자동으로 정리하는 스케줄러
 */
@Service
public class ReceiptCleanupScheduler {
    private static final Logger logger = LoggerFactory.getLogger(ReceiptCleanupScheduler.class);

    @Value("${receipt.temp.directory}")
    private String tempDirectory;

    @Value("${receipt.temp.cleanup-hours:1}")
    private int cleanupHours;

    /**
     * 매 시간마다 실행되어 오래된 파일 삭제
     * cron: "초 분 시 일 월 요일"
     * "0 0 * * * *" = 매 시간 정각
     */
    @Scheduled(cron = "0 0 * * * *")
    public void cleanupOldReceipts() {
        logger.info("Starting scheduled cleanup of old receipt files...");

        try {
            Path tempDir = Paths.get(tempDirectory);

            // 디렉토리가 존재하지 않으면 스킵
            if (!Files.exists(tempDir)) {
                logger.info("Temp directory does not exist: {}", tempDirectory);
                return;
            }

            // 현재 시간에서 cleanupHours 시간 전
            Instant cutoffTime = Instant.now().minusSeconds(cleanupHours * 3600L);

            // 오래된 파일 삭제
            int deletedCount = 0;
            int errorCount = 0;

            try (Stream<Path> files = Files.list(tempDir)) {
                for (Path file : (Iterable<Path>) files::iterator) {
                    try {
                        // 파일인지 확인 (디렉토리는 스킵)
                        if (!Files.isRegularFile(file)) {
                            continue;
                        }

                        // 파일 생성 시간 확인
                        FileTime lastModifiedTime = Files.getLastModifiedTime(file);
                        if (lastModifiedTime.toInstant().isBefore(cutoffTime)) {
                            // 오래된 파일 삭제
                            Files.delete(file);
                            deletedCount++;
                            logger.debug("Deleted old receipt file: {}", file.getFileName());
                        }
                    } catch (IOException e) {
                        errorCount++;
                        logger.warn("Failed to delete file: {}", file.getFileName(), e);
                    }
                }
            }

            logger.info("Cleanup completed: {} files deleted, {} errors", deletedCount, errorCount);

        } catch (IOException e) {
            logger.error("Error during cleanup process", e);
        } catch (Exception e) {
            logger.error("Unexpected error during cleanup", e);
        }
    }

    /**
     * 수동으로 정리 실행 (테스트용)
     */
    public void manualCleanup() {
        logger.info("Manual cleanup triggered");
        cleanupOldReceipts();
    }
}
