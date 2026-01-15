package com.example.kakaoonboarding.dto.response;

import java.time.LocalDateTime;

public class ReportStatusResponse {
    private String requestId;
    private String status;
    private Integer progress;  // 0-100
    private String reportId;
    private String message;
    private String fileSize;
    private LocalDateTime createdAt;
    private String downloadUrl;

    public ReportStatusResponse(String requestId, String status, Integer progress,
                               String reportId, String message, String fileSize,
                               LocalDateTime createdAt, String downloadUrl) {
        this.requestId = requestId;
        this.status = status;
        this.progress = progress;
        this.reportId = reportId;
        this.message = message;
        this.fileSize = fileSize;
        this.createdAt = createdAt;
        this.downloadUrl = downloadUrl;
    }

    // Getters and Setters
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
