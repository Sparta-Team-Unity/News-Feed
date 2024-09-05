package com.sparta.newsfeed.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserSignUpRequestDto extends UserLoginRequestDto {
    protected String name;
}
