package com.sparta.newsfeed.domain.repository;

import com.sparta.newsfeed.domain.entity.BlacklistToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlacklistTokenRepository extends JpaRepository<BlacklistToken, Integer> {
    Optional<BlacklistToken> findByBlacklistToken(String blacklistTokenId);
}
