package com.sparta.newsfeed.domain.dto;


import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class WaitsDto {
    private int userId;
    private LocalDateTime time;
    public WaitsDto(int userId, LocalDateTime time) {
        this.userId = userId;
        this.time = time;
    }
}
