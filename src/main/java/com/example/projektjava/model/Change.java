package com.example.projektjava.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Change implements Serializable {
    private String oldValue;
    private String newValue;
    private String table;
    private Long userId;
    private LocalDateTime dateTime;
    private String changeType;

    public Change(String oldValue, String newValue, String table, Long userId, LocalDateTime dateTime, String changeType) {
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.table = table;
        this.userId = userId;
        this.dateTime = dateTime;
        this.changeType = changeType;
    }

    public static class Builder {
        private String oldValue;
        private String newValue;
        private String table;
        private Long userId;
        private LocalDateTime dateTime;
        private String changeType;

        public Builder oldValue(String oldValue) {
            this.oldValue = oldValue;
            return this;
        }

        public Builder newValue(String newValue) {
            this.newValue = newValue;
            return this;
        }

        public Builder table(String table) {
            this.table = table;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder dateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public Builder changeType(String changeType) {
            this.changeType = changeType;
            return this;
        }

        public Change build() {
            return new Change(oldValue, newValue, table, userId, dateTime, changeType);
        }
    }

    // Getters and setters
    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }
}
