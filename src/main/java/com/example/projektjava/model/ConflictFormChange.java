package com.example.projektjava.model;

import com.example.projektjava.enums.StatusEnum;

import java.time.LocalDate;
import java.util.List;

public class ConflictFormChange {
    private List<String> userInvolved;
    private String description;
    private StatusEnum status;
    private LocalDate date;

    public List<String> getUserInvolved() {
        return userInvolved;
    }

    public void setUserInvolved(List<String> userInvolved) {
        this.userInvolved = userInvolved;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ConflictFormChange(List<String> userInvolved, String description, StatusEnum status, LocalDate date) {
        this.userInvolved = userInvolved;
        this.description = description;
        this.status = status;
        this.date = date;
    }
}