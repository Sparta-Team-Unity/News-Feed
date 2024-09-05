package com.sparta.newsfeed.config.filter;

import com.sparta.newsfeed.domain.entity.User;
import com.sparta.newsfeed.domain.exception.ErrorCode;
import com.sparta.newsfeed.domain.exception.UnityException;
import com.sparta.newsfeed.domain.repository.UserRepository;
import com.sparta.newsfeed.domain.service.BlacklistTokenService;
import com.sparta.newsfeed.domain.service.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.util.StringUtils;

import java.io.IOException;

@EnableWebSecurity(debug = true)
@Slf4j(topic = "JwtFilter")
@Order(1)
public class JwtFilter implements Filter {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;
    private final BlacklistTokenService blacklistTokenService;

    public JwtFilter(JwtUtil jwtUtil, UserRepository userRepository, TokenService tokenService, BlacklistTokenService blacklistTokenService) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.blacklistTokenService = blacklistTokenService;
    }

    /**
     * 1차 필터 / 유효한 Jwt인지 확인하는 메서드
     * @param servletRequest 요청 Servlet
     * @param servletResponse 반환 Servlet
     * @param filterChain FilterChain객체
     * @throws IOException 다음 체인에서의 예외 처리를 위한 Exception
     * @throws ServletException 다음 체인에서의 예외 처리를 위한 Exception
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("doFilter");
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        // 토큰이 필요 없는 URL인지 확인 ( 회원가입, 로그인 )
        if (!isNeedTokenURL(httpServletRequest)) {
            filterChain.doFilter(httpServletRequest, servletResponse);
        } else {
            tokenFilter(httpServletRequest, servletResponse, filterChain);
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    /**
     * 토큰 필터 메인 로직
     * @param servletRequest User정보를 탑제할 요청이 들어온 정보
     * @param servletResponse 토큰 갱신이 필요하다면 갱신할 반환 정보
     */
    private void tokenFilter(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {
        String tokenValue = jwtUtil.getTokenFromRequest((HttpServletRequest) servletRequest);

        if (isBlacklistToken(tokenValue)) {
            throw new UnityException(ErrorCode.INVALID_TOKEN);
        }

        // token에 값이 존재하는지 확인
        if (StringUtils.hasText(tokenValue)) {
            // token 내용 추출
            String token = jwtUtil.substringToken(tokenValue);
            // 해당 토큰 관련된 유저가 있는지 확인
            int userId = getUserIdFromTokenValue(tokenValue);
            if(userId == -1) {
                throw new UnityException(ErrorCode.EXPIRED_TOKEN);
            }

            // 토큰에 실린 id를 통해 유저 조회
            User user = userRepository.findById(userId).orElseThrow(
                    () -> new NullPointerException("Not Found User")
            );

            // access 토큰을 갱신한다.
            refreshAccessToken((HttpServletResponse) servletResponse, user);
            // request에 유저 정보 탑제
            servletRequest.setAttribute("user", user);
        } else {
            throw new IllegalArgumentException("Token Not Found");
        }
    }

    /**
     * 이미 blacklist에 등록된 토큰인지 확인하는 메서드
     * @param token 확인할 토큰
     * @return true : BlackList에 이미 등록된 상태 / false : 아직 등록이 안된 상태
     */
    private boolean isBlacklistToken(String token) {
        return blacklistTokenService.isBlacklisted(token);
    }

    /**
     * AccessToken을 갱신하는 메서드
     * @param httpServletResponse Token을 갖고 있을 HttpServletResponse 객체
     * @param user AccessToken을 갱신할 유저
     */
    private void refreshAccessToken(HttpServletResponse httpServletResponse, User user) {
        try {
            String newToken = tokenService.refreshAccessToken(user);

            if(newToken == null) {
                throw new IllegalArgumentException("Need Login");
            }

            jwtUtil.addJwtToCookie(httpServletResponse, newToken);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 토큰을 사용하는 url인지 확인하는 메서드
     * @param request 들어온 요청
     * @return 토큰 사용하는 url여부
     */
    private boolean isNeedTokenURL(HttpServletRequest request) {
        String url = request.getRequestURI();
        String method = request.getMethod();

        return method.compareTo("DELETE") == 0 || !(StringUtils.hasText(url) && url.startsWith("/api/users"));
    }

    /**
     * Token으로부터 userId를 추출해내는 함수
     * @param tokenValue 추출할 Token값
     * @return -1 : access, refresh 토큰 둘 다 만료된 경우 / 그 외 : userId
     */
    private int getUserIdFromTokenValue(String tokenValue) {
        String token = jwtUtil.substringToken(tokenValue);
        int userId = 0;

        try {
            userId = jwtUtil.getUserIdFromToken(token);
        } catch(ExpiredJwtException e) {
            // 토큰이 만료된 경우, refresh 토큰으로 체크
            String refreshToken = tokenService.getRefreshTokenByAccessToken(tokenValue);

            try {
                userId = jwtUtil.getUserIdFromToken(refreshToken);
            } catch(ExpiredJwtException e2) {
                return -1;
            }
        }

        return userId;
    }
}
