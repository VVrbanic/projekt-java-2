package com.example.projektjava.model;

public class UserPrint<T, G> {
    private T name;
    private G id;

    public T getName() {
        return name;
    }

    public void setName(T name) {
        this.name = name;
    }

    public G getId() {
        return id;
    }

    public void setId(G id) {
        this.id = id;
    }

    public UserPrint(T name, G id) {
        this.name = name;
        this.id = id;
    }
}

