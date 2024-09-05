package com.sparta.newsfeed.domain.dto;

import lombok.Getter;

@Getter
public class FollowDto {
    private int userId;

    public FollowDto(int userId) {

        this.userId = userId;
    }
}
