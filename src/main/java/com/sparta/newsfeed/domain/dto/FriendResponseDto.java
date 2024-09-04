package com.sparta.newsfeed.domain.dto;

import java.time.LocalDateTime;
import java.util.List;


public class FriendResponseDto {
    List<FollowDto> follows;

    public FriendResponseDto(List<FollowDto> follows) {
        this.follows = follows;
    }
}
