package com.example.projektjava;

import com.example.projektjava.model.User;

public class UserSession {
    private static UserSession instance;

    private User user;

    private UserSession(User user) {
        this.user = user;
    }

    public static void init(User user) {
        if (instance == null) {
            instance = new UserSession(user);
        }
    }

    public static UserSession getInstance() {
        return instance;
    }

    public User getUser() {
        return user;
    }

    public static void clear() {
        instance = null;
    }
}

