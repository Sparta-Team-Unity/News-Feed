package com.sparta.newsfeed.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class BlacklistToken {
    @Id
    private String blacklistToken;

    public BlacklistToken(String blacklistToken) {
        this.blacklistToken = blacklistToken;
    }
}
