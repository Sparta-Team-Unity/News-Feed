package com.sparta.newsfeed.domain.service;

import com.sparta.newsfeed.domain.entity.BlacklistToken;
import com.sparta.newsfeed.domain.repository.BlacklistTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BlacklistTokenService {

    private final BlacklistTokenRepository blacklistTokenRepository;

    public BlacklistTokenService(BlacklistTokenRepository blacklistTokenRepository) {
        this.blacklistTokenRepository = blacklistTokenRepository;
    }

    @Transactional(readOnly = true)
    public boolean isBlacklisted(String token) {
        return blacklistTokenRepository.findByBlacklistToken(token).isPresent();
    }

    /**
     *
     * @param token
     */
    @Transactional
    public void addBlacklistToken(String token) {
        BlacklistToken blacklistToken  = new BlacklistToken(token);

        blacklistTokenRepository.save(blacklistToken);
    }
}
