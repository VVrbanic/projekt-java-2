package com.example.projektjava.model;

import com.example.projektjava.enums.StatusEnum;

import java.time.LocalDate;
import java.util.List;

public class ConflictForm {
    private Long id;
    private User reporter;
    private List<User> userInvolved;
    private String description;
    private StatusEnum status;
    private LocalDate date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getReporter() {
        return reporter;
    }

    public void setReporter(User reporter) {
        this.reporter = reporter;
    }

    public List<User> getUserInvolved() {
        return userInvolved;
    }

    public void setUserInvolved(List<User> userInvolved) {
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
}
