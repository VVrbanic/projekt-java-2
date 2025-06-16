package com.example.projektjava.model;

import java.time.LocalDateTime;

public class Conflict {
    private Long id;
    //User table
    private Long reporter_id;
    private String description;
    //Status table
    private Long status_id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    //User table
    private Long updated_by;

}
