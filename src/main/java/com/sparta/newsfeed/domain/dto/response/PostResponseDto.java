package com.sparta.newsfeed.domain.dto.response;

import com.sparta.newsfeed.domain.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {

    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime editAt;
    private final Long user; //user 받아와야 함

    public PostResponseDto(Post post) {

    }
}
