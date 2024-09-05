package com.sparta.newsfeed.domain.repository;

import com.sparta.newsfeed.domain.entity.Token;
import com.sparta.newsfeed.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findByUser(User user);
    Optional<Token> findByAccessToken(String accessToken);
}
