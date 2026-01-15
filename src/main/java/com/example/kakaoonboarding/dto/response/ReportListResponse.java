package com.example.kakaoonboarding.dto.response;

import com.example.kakaoonboarding.entity.ReportStatus;
import com.example.kakaoonboarding.entity.ReportType;

import java.time.LocalDateTime;
import java.util.List;

public class ReportListResponse {
    private List<ReportDto> data;

    public ReportListResponse(List<ReportDto> data) {
        this.data = data;
    }

    // Getters and Setters
    public List<ReportDto> getData() {
        return data;
    }

    public void setData(List<ReportDto> data) {
        this.data = data;
    }

    public static class ReportDto {
        private String id;
        private String title;
        private String period;
        private ReportType type;
        private String fileSize;
        private ReportStatus status;
        private String createdAt;
        private String downloadUrl;
        private String expiresAt;

        public ReportDto(String id, String title, String period, ReportType type,
                        String fileSize, ReportStatus status, String createdAt,
                        String downloadUrl, String expiresAt) {
            this.id = id;
            this.title = title;
            this.period = period;
            this.type = type;
            this.fileSize = fileSize;
            this.status = status;
            this.createdAt = createdAt;
            this.downloadUrl = downloadUrl;
            this.expiresAt = expiresAt;
        }

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public ReportType getType() {
            return type;
        }

        public void setType(ReportType type) {
            this.type = type;
        }

        public String getFileSize() {
            return fileSize;
        }

        public void setFileSize(String fileSize) {
            this.fileSize = fileSize;
        }

        public ReportStatus getStatus() {
            return status;
        }

        public void setStatus(ReportStatus status) {
            this.status = status;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }

        public String getExpiresAt() {
            return expiresAt;
        }

        public void setExpiresAt(String expiresAt) {
            this.expiresAt = expiresAt;
        }
    }
}
