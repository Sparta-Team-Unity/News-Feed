package com.sparta.newsfeed.domain.dto.request;

import lombok.Getter;

@Getter
public class PostRequestDto {
    private Long userId;
    private String title;
    private String content;
}
