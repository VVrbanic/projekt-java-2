package com.example.projektjava.enums;

public enum ChangeTypeEnum {
    NEW(1L, "New"),
    EDIT(2L,"Edit"),
    DELETE(3L, "Delete");


    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    ChangeTypeEnum(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ChangeTypeEnum getNameById(Long id) {
        for (ChangeTypeEnum status : ChangeTypeEnum.values()) {
            if (status.getId().equals(id)) {
                return status;
            }
        }
        return null;
    }

}

