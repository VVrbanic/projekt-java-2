package com.example.projektjava.enums;

public enum UserRoleEnum {
    ADMIN(1, "Admin"),
    USER(2,"User");


    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    UserRoleEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

