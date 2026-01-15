package com.example.kakaoonboarding.dto.response;

import java.time.LocalDateTime;

public class ReportGenerateResponse {
    private String requestId;
    private String status;
    private String message;
    private Integer estimatedTime;  // 초 단위
    private String notificationEmail;
    private LocalDateTime expectedCompletionTime;

    public ReportGenerateResponse(String requestId, String status, String message,
                                 Integer estimatedTime, String notificationEmail,
                                 LocalDateTime expectedCompletionTime) {
        this.requestId = requestId;
        this.status = status;
        this.message = message;
        this.estimatedTime = estimatedTime;
        this.notificationEmail = notificationEmail;
        this.expectedCompletionTime = expectedCompletionTime;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getNotificationEmail() {
        return notificationEmail;
    }

    public void setNotificationEmail(String notificationEmail) {
        this.notificationEmail = notificationEmail;
    }

    public LocalDateTime getExpectedCompletionTime() {
        return expectedCompletionTime;
    }

    public void setExpectedCompletionTime(LocalDateTime expectedCompletionTime) {
        this.expectedCompletionTime = expectedCompletionTime;
    }
}
