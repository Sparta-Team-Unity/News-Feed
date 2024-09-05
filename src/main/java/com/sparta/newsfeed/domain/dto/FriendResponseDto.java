package com.sparta.newsfeed.domain.dto;

import java.util.List;


public class FriendResponseDto {
    List<FollowDto> follows;

    public FriendResponseDto(List<FollowDto> follows) {
        this.follows = follows;
    }
}
