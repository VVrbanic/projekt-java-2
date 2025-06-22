package com.example.projektjava.model;

public class ConflictUsers extends Id {
    private Long conflictId;
    private Long userId;

    private ConflictUsers(Builder builder) {
        super(builder.id);
        this.conflictId = builder.conflictId;
        this.userId = builder.userId;
    }

    public Long getConflictId() {
        return conflictId;
    }

    public void setConflictId(Long conflictId) {
        this.conflictId = conflictId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public static class Builder {
        private Long id;
        private Long conflictId;
        private Long userId;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setConflictId(Long conflictId) {
            this.conflictId = conflictId;
            return this;
        }

        public Builder setUserId(Long userId) {
            this.userId = userId;
            return this;
        }

        public ConflictUsers build() {
            return new ConflictUsers(this);
        }
    }
}