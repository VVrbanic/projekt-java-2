package com.example.projektjava.model;

import java.time.LocalDate;

public class ConflictDTO {
    private Long id;
    private Long reporterId;
    private String description;
    private Long statusId;
    private LocalDate date;

    private ConflictDTO(Builder builder) {
        this.id = builder.id;
        this.reporterId = builder.reporterId;
        this.description = builder.description;
        this.statusId = builder.statusId;
        this.date = builder.date;
    }

    public static class Builder {
        private Long id;
        private Long reporterId;
        private String description;
        private Long statusId;
        private LocalDate date;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder reporterId(Long reporterId) {
            this.reporterId = reporterId;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder statusId(Long statusId) {
            this.statusId = statusId;
            return this;
        }

        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public ConflictDTO build() {
            return new ConflictDTO(this);
        }
    }

    public Long getId() {
        return id;
    }

    public Long getReporterId() {
        return reporterId;
    }

    public String getDescription() {
        return description;
    }

    public Long getStatusId() {
        return statusId;
    }

    public LocalDate getDate() {
        return date;
    }
}
