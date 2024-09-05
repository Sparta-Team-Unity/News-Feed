package com.sparta.newsfeed.domain.dto.profile;

import lombok.Getter;

@Getter
public class ProfileSaveRequestDto {
    private String currentPassword;
    private String newPassword;
}
