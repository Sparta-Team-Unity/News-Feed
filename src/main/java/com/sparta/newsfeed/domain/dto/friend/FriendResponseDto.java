package com.sparta.newsfeed.domain.dto.friend;

import lombok.Getter;

import java.util.List;

@Getter
public class FriendResponseDto {
    List<FollowDto> follows;

    public FriendResponseDto(List<FollowDto> follows) {
        this.follows = follows;
    }
}
