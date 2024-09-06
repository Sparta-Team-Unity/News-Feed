package com.sparta.newsfeed.domain.service;

import com.sparta.newsfeed.config.filter.JwtUtil;
import com.sparta.newsfeed.domain.entity.BlacklistToken;
import com.sparta.newsfeed.domain.repository.BlacklistTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BlacklistTokenService {

    private final BlacklistTokenRepository blacklistTokenRepository;
    private final JwtUtil jwtUtil;

    public BlacklistTokenService(BlacklistTokenRepository blacklistTokenRepository, JwtUtil jwtUtil) {
        this.blacklistTokenRepository = blacklistTokenRepository;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 이미 블랙리스트에 등록된 토큰인지 확인하는 메서드
     * @param token 확인할 토큰 내용
     * @return True : 이미 블랙리스트 등록된 상태 / False : 등록되지 않은 상태
     */
    @Transactional(readOnly = true)
    public boolean isBlacklisted(String token) {
        return blacklistTokenRepository.findByBlacklistToken(token).isPresent();
    }

    /**
     * 해당 토큰을 BlackList에 등록하는 코드
     * @param token 블랙리스트에 등록할 토큰
     */
    @Transactional
    public void addBlacklistToken(String token) {
        BlacklistToken blacklistToken  = new BlacklistToken(token);

        blacklistTokenRepository.save(blacklistToken);
    }

    /**
     * BlackListToken에 기간이 만료된 토큰을 삭제하는 메서드
     */
    @Transactional
    public void clearExpirestoken() {
        List<BlacklistToken> blacklistTokens = blacklistTokenRepository.findAll();

        for (BlacklistToken blacklistToken : blacklistTokens) {
            String token = jwtUtil.substringToken(blacklistToken.getBlacklistToken());

            if(!jwtUtil.validateToken(token)) {
                blacklistTokenRepository.delete(blacklistToken);
            }
        }
    }
}
