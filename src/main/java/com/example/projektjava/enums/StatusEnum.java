package com.example.projektjava.enums;

public enum StatusEnum {
    NEW(1L, "New"),
    IN_REVIEW(2L,"In review"),
    RESOLVED(3L, "Resolved");


    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    StatusEnum(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StatusEnum getNameById(Long id) {
        for (StatusEnum status : StatusEnum.values()) {
            if (status.getId().equals(id)) {
                return status;
            }
        }
        return null;
    }


}

