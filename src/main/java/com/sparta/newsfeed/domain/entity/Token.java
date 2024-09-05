package com.sparta.newsfeed.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    User user;

    @Column(length = 255)
    private String refreshToken;

    @Column(length = 255)
    private String accessToken;

    public Token(User user, String refreshToken, String accessToken) {
        this.user = user;
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }

    public void refresh(String refreshToken, String accessToken) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
