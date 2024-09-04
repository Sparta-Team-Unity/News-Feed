package com.sparta.newsfeed.domain.dto;

import lombok.Getter;

@Getter
public class PostUpdateRequestDto {
    private Integer userId;
    private String title;
    private String contents;
}
