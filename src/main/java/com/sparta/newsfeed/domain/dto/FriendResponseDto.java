package com.sparta.newsfeed.domain.dto;

import java.time.LocalDateTime;
import java.util.List;


public class FriendResponseDto {
    List<FollowDto> follows;
    List<WaitsDto> waits;
    LocalDateTime createdAt;

    public FriendResponseDto(List<FollowDto> follows) {
        this.follows = follows;
    }

    public FriendResponseDto( List<WaitsDto> waits,LocalDateTime createdAt) {
        this.waits = waits;
        this.createdAt = createdAt;
    }
}
