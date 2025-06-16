package com.example.projektjava.enums;

public enum Statuses {
    NEW(1, "New"),
    IN_REVIEW(2,"In review"),
    RESOLVED(3, "Resolved");


    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    Statuses(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

