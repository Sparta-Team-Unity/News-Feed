package com.sparta.newsfeed.domain.dto;


import java.time.LocalDateTime;

public class WaitsDto {
    private int userId;
    private LocalDateTime time;
    public WaitsDto(int userId, LocalDateTime time) {
        this.userId = userId;
        this.time = time;
    }
}
