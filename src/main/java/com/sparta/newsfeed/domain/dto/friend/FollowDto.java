package com.sparta.newsfeed.domain.dto.friend;

import lombok.Getter;

@Getter
public class FollowDto {
    private int userId;
    private String userName;

    public FollowDto(int userId, String userName) {

        this.userId = userId;
        this.userName = userName;
    }
}
