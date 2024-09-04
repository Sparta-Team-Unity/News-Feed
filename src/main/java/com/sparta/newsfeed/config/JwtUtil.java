package com.sparta.newsfeed.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // Bearer을 작성 함으로서 토큰임을 명시
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 유지 시간 60
    private final long TOKEN_EXPIRES_IN_SECONDS = 60 * 60 * 1000;

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public static final Logger logger = LoggerFactory.getLogger("JWT Log");

    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey.getBytes());
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 토큰을 생성하는 메서드
     * @param userId 토큰에 담길 userId
     * @return userId가 포함된 토큰
     */
    public String createToken(int userId) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(userId))
                        .setExpiration(new Date(date.getTime() + TOKEN_EXPIRES_IN_SECONDS))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    /**
     * JWT 쿠키에 저장하는 메서드
     * @param response 쿠키를 추가할 Response 객체
     * @param token 추가할 Cookie에 담길 token
     */
    public void addJwtToCookie(HttpServletResponse response, String token) {
        token = URLEncoder.encode(token, StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token);
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    /**
     * JWT 토큰 식별자를 확인하고 식별자 이후의 값을 가져오는 메서드
     * @param token 식별자를 제외할 token
     * @return 식별자가 제외된 token
     */
    public String substringToken(String token) {
        if(StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            return token.substring(BEARER_PREFIX.length());
        }
        logger.error("Token Not Found");
        throw new NullPointerException("Token Not Found");
    }

    /**
     * 유효한 토큰인지 확인하는 메서드
     * @param token 확인할 토큰
     * @return 유효 여부
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            logger.error("Invalid Token");
        } catch(ExpiredJwtException e) {
            logger.error("Expired Token");
        } catch(UnsupportedJwtException e) {
            logger.error("Unsupported Jwt");
        }
        return false;
    }

    /**
     * 토큰에서 사용자 정보를 읽어오는 메서드
     * @param token 정보를 읽어올 토큰
     * @return 토큰에 담겨있는 사용자 정보
     */
    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    /**
     * 토큰에서 UserId를 읽어오는 메서드
     * @param token UserId를 읽어올 Token
     * @return 토큰에 담긴 UserId
     */
    public int getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        String userId = claims.getSubject();

        return Integer.parseInt(userId);
    }

    /**
     * 요청에서 Token값을 가져오는 메서드
     * @param request 요청 Servlet
     * @return Token값 / null : Token이 존재하지 않음
     */
    public String getTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    return URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                }
            }
        }

        return null;
    }
}
