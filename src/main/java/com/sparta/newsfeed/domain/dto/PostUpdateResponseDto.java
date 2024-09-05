package com.sparta.newsfeed.domain.dto;

import com.sparta.newsfeed.domain.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostUpdateResponseDto {
    private Integer id;
    private String title;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime editAt;
    private String userName;


    public PostUpdateResponseDto(
            Integer id,
            String title,
            String contents,
            LocalDateTime createAt,
            LocalDateTime editAt,
            User user) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.createdAt = createAt;
        this.editAt = editAt;
        this.userName = user.getName();

    }
}
