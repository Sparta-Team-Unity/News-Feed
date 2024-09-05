package com.sparta.newsfeed.domain.dto;


import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class WaitsDto {
    private int userId;
    private LocalDateTime time;
    private String userName;
    public WaitsDto(int userId, LocalDateTime time, String userName) {
        this.userId = userId;
        this.time = time;
        this.userName = userName;
    }
}
