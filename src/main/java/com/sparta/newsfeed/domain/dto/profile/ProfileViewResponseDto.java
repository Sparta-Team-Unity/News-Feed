package com.sparta.newsfeed.domain.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ProfileViewResponseDto {
    private String email;
    private LocalDateTime createAt;

    public void maskPrivacy() {
        this.email = "*****";
    }
}
