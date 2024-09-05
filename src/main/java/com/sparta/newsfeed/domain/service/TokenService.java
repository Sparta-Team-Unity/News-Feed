package com.sparta.newsfeed.domain.service;

import com.sparta.newsfeed.config.JwtUtil;
import com.sparta.newsfeed.domain.entity.Token;
import com.sparta.newsfeed.domain.entity.User;
import com.sparta.newsfeed.domain.exception.ErrorCode;
import com.sparta.newsfeed.domain.exception.UnityException;
import com.sparta.newsfeed.domain.repository.TokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private final JwtUtil jwtUtil;

    public TokenService(TokenRepository tokenRepository, JwtUtil jwtUtil) {
        this.tokenRepository = tokenRepository;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 해당 유저의 Token 값을 받아오는 메서드
     * @param user Token값을 받을 유저
     * @return 해당 유저의 Token
     */
    @Transactional
    public Token getToken(User user) {
        Optional<Token> token = tokenRepository.findByUser(user);

        return token.orElse(null);
    }

    /**
     * AccessToken을 갱신하는 메서드
     * @param user accessToken을 갱신할 유저
     * @return 갱신된 AccessToken
     */
    @Transactional
    public String refreshAccessToken(User user) throws Exception {
        Token token = getToken(user);
        String tokenBody = jwtUtil.substringToken(token.getRefreshToken());

        // Refresh토큰도 만료됐을경우 재로그인 필요
        if(!jwtUtil.validateToken(tokenBody)) {
            throw new UnityException(ErrorCode.EXPIRED_TOKEN);
        }

        String accessToken = jwtUtil.createAccessToken(user.getUserId());

        // 유저 access 토큰 갱신
        token.setAccessToken(accessToken);
        tokenRepository.save(token);

        return accessToken;
    }

    /**
     *
     * @param user
     * @throws Exception
     */
    @Transactional
    public void eraseToken(User user) throws Exception {
        tokenRepository.deleteById(getToken(user).getId());
    }

    /**
     * 로그인시 모든 토큰을 재발급 하는 메서드
     * @param user 유저 정보
     * @return 생성된 토큰 객체
     */
    @Transactional
    public Token createToken(User user) {
        Token userToken = getToken(user);

        String accessToken = jwtUtil.createAccessToken(user.getUserId());
        String refreshToken = jwtUtil.createRefreshToken(user.getUserId());

        if (userToken != null) {
            userToken.refresh(refreshToken, accessToken);
        } else {
            userToken = new Token(user, refreshToken, accessToken);

            tokenRepository.save(userToken);
        }

        return userToken;
    }

    /**
     *
     * @param accessTokenValue
     * @return
     */
    public String getRefreshTokenByAccessToken(String accessTokenValue) {
        Token token = tokenRepository.findByAccessToken(accessTokenValue).orElseThrow();

        return jwtUtil.substringToken(token.getRefreshToken());
    }
}
