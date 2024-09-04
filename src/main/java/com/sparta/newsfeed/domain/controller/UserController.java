package com.sparta.newsfeed.domain.controller;

import com.sparta.newsfeed.config.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final JwtUtil jwtUtil;

    public UserController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/create-jwt/{userId}")
    public String createJwt(HttpServletResponse response, @PathVariable Integer userId) {
        String token = jwtUtil.createToken(userId);
        jwtUtil.addJwtToCookie(response, token);

        return "createJwt : " + token;
    }

    /**
     * 토큰 사용법 예시
     * @param tokenValue AUTHORIZATION_HEADER 를 사용하는 cookie
     * @return cookie에서 추출한 userId
     */
    @GetMapping("/get-jwt")
    public int testJwt(@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue) {
        String token = jwtUtil.substringToken(tokenValue);

        if(!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token Error");
        }

        int userId = jwtUtil.getUserIdFromToken(token);
        System.out.println(userId);

        return userId;
    }
}
