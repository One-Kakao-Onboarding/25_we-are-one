package com.example.kakaoonboarding.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;  // 보고서 제목

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType type;  // 보고서 유형

    @Column(nullable = false)
    private LocalDate startDate;  // 보고서 시작 날짜

    @Column(nullable = false)
    private LocalDate endDate;  // 보고서 종료 날짜

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status;  // 보고서 상태

    private String fileName;  // 파일명

    private String filePath;  // 파일 경로

    private Long fileSize;  // 파일 크기 (bytes)

    @Column(length = 1000)
    private String errorMessage;  // 에러 메시지 (생성 실패시)

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;  // 생성 시간

    private LocalDateTime completedAt;  // 완료 시간

    private LocalDateTime expiresAt;  // 만료 시간

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == ReportStatus.READY) {
            completedAt = LocalDateTime.now();
            expiresAt = LocalDateTime.now().plusMonths(1);  // 1개월 후 만료
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ReportType getType() {
        return type;
    }

    public void setType(ReportType type) {
        this.type = type;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
