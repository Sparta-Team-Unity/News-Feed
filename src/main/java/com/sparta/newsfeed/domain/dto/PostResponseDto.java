package com.sparta.newsfeed.domain.dto;

import com.sparta.newsfeed.domain.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {

    private Integer id;
    private String title;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime editAt;
    private Integer user; //user 받아와야 함

    public PostResponseDto(Post post) {

    }
}
