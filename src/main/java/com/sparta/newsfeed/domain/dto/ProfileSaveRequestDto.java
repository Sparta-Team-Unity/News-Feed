package com.sparta.newsfeed.domain.dto;

import lombok.Getter;

@Getter
public class ProfileSaveRequestDto {
    private String currentPassword;
    private String newPassword;
}
