package com.sparta.newsfeed.domain.dto.user;

import lombok.*;

@Builder
@Getter
@ToString
@AllArgsConstructor
public class UserDto {
    int id;
    String userEmail;
    String username;
}
