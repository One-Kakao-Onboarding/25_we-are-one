package com.example.kakaoonboarding.dto.request;

import com.example.kakaoonboarding.entity.ReportType;

public class ReportGenerateRequest {
    private ReportType type;
    private Period period;
    private String format;  // pdf 또는 xlsx
    private IncludeScope includeScope;

    // Getters and Setters
    public ReportType getType() {
        return type;
    }

    public void setType(ReportType type) {
        this.type = type;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public IncludeScope getIncludeScope() {
        return includeScope;
    }

    public void setIncludeScope(IncludeScope includeScope) {
        this.includeScope = includeScope;
    }

    public static class Period {
        private String startDate;
        private String endDate;

        // Getters and Setters
        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }
    }

    public static class IncludeScope {
        private Boolean scope3;
        private Boolean departmentBreakdown;
        private Boolean recommendations;
        private Boolean evidenceDocuments;

        // Getters and Setters
        public Boolean getScope3() {
            return scope3;
        }

        public void setScope3(Boolean scope3) {
            this.scope3 = scope3;
        }

        public Boolean getDepartmentBreakdown() {
            return departmentBreakdown;
        }

        public void setDepartmentBreakdown(Boolean departmentBreakdown) {
            this.departmentBreakdown = departmentBreakdown;
        }

        public Boolean getRecommendations() {
            return recommendations;
        }

        public void setRecommendations(Boolean recommendations) {
            this.recommendations = recommendations;
        }

        public Boolean getEvidenceDocuments() {
            return evidenceDocuments;
        }

        public void setEvidenceDocuments(Boolean evidenceDocuments) {
            this.evidenceDocuments = evidenceDocuments;
        }
    }
}
