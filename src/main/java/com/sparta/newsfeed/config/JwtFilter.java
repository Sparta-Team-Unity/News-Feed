package com.sparta.newsfeed.config;

import com.sparta.newsfeed.domain.entity.User;
import com.sparta.newsfeed.domain.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@EnableWebSecurity(debug = true)
@Slf4j(topic = "JwtFilter")
@Component
@Order(1)
public class JwtFilter implements Filter {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
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

        System.out.println("Do Filter");
        // 토큰이 필요 없는 URL인지 확인 ( 회원가입, 로그인 )
        if (!isNeedTokenURL(httpServletRequest)) {
            filterChain.doFilter(httpServletRequest, servletResponse);
        } else {
            String tokenValue = jwtUtil.getTokenFromRequest(httpServletRequest);

            if (StringUtils.hasText(tokenValue)) {
                String token = jwtUtil.substringToken(tokenValue);

                if (!jwtUtil.validateToken(token)) {
                    throw new IllegalArgumentException("Token invalid");
                }

                User user = userRepository.findById(jwtUtil.getUserIdFromToken(token)).orElseThrow(
                        () -> new NullPointerException("Not Found User")
                );

                servletRequest.setAttribute("user", user);
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                throw new IllegalArgumentException("Token Not Found");
            }
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
}
