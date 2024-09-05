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

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // Bearer을 작성 함으로서 토큰임을 명시
    public static final String BEARER_PREFIX = "Bearer ";
    // 리프래쉬 토큰 유지 시간 1시간
    private final long REFRESH_TOKEN_EXPIRES_IN_SECONDS = 60 * 60 * 1000; // 60 * 60 * 1000;
    // 액세스 토큰 유지 시간 10분
    private final long ACCESS_TOKEN_EXPIRES_IN_SECONDS = 60 * 1000;

    private final String CLAIM_USER_ID = "userId";
    private final String CLAIM_TOKEN_TYPE = "tokenType";
    private final String ACCESS_TOKEN = "accessToken";
    private final String REFRESH_TOKEN = "refreshToken";


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
    public String createToken(int userId, String tokenType, Date date, long expiration) {
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(userId))
                        .setExpiration(new Date(date.getTime() + expiration))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .addClaims(getClaims(userId, tokenType))
                        .compact();
    }

    /**
     * RefreshToken을 만들어주는 메서드
     * @param userId 유저 아이디
     * @return 생성된 RefreshToken
     */
    public String createRefreshToken(int userId) {
        return createToken(userId, REFRESH_TOKEN, new Date(), REFRESH_TOKEN_EXPIRES_IN_SECONDS);
    }

    /**
     * AccessToken을 만드는 메서드
     * @param userId 유저 아이디
     * @return 생성된 AccessToken
     */
    public String createAccessToken(int userId) {
        return createToken(userId, ACCESS_TOKEN, new Date(), ACCESS_TOKEN_EXPIRES_IN_SECONDS);
    }

    /**
     * 필요 정보를 Map형태로 반환해주는 메서드
     * @param userId 유저 Id
     * @param tokenType 토큰 타입
     * @return 해당 정보가 Key, Value형태로 작성된 Map
     */
    private Map<String, Object> getClaims(int userId, String tokenType) {
        Map<String, Object> claims = new HashMap<>();

        claims.put(CLAIM_USER_ID, String.valueOf(userId));
        claims.put(CLAIM_TOKEN_TYPE, tokenType);

        return claims;
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
            Date date = new Date();
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            // 만료된 토큰인지 확인
            return !claimsJws.getBody().getExpiration().before(date);
        } catch (SecurityException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            logger.error("Invalid Token");
        } catch (ExpiredJwtException exception) {
            return false;
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported Jwt");
        }
        return false;
    }

    /**
     * 토큰에서 사용자 정보를 읽어오는 메서드
     * @param token 정보를 읽어올 토큰
     * @return 토큰에 담겨있는 사용자 정보
     */
    public Claims getClaimsFromToken(String token) throws ExpiredJwtException {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    /**
     * 토큰에서 UserId를 읽어오는 메서드
     * @param token UserId를 읽어올 Token
     * @return 토큰에 담긴 UserId
     */
    public int getUserIdFromToken(String token) throws ExpiredJwtException {
            Claims claims = getClaimsFromToken(token);
            String userId = claims.get(CLAIM_USER_ID, String.class);

            return Integer.parseInt(userId);
    }

    /**
     * 요청에서 Token값을 가져오는 메서드
     * @param request 요청 Servlet
     * @return Token값 / null : Token이 존재하지 않음
     */
    public String getTokenFromRequest(HttpServletRequest request) {
        Cookie cookie = findTokenCookie(request);
        if(cookie != null) {
            return URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
        }

        return null;
    }

    /**
     * Cookie에 있는 Jwt토큰을 삭제하는 함수
     * @param request Token이 담겨 있는 request Servlet
     */
    public void deleteJwtInCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = findTokenCookie(request);

        if(cookie != null) {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    /**
     * request Servlet에 담겨 있는 Token객체를 반환하는 함수
     * @param request Token이 담겨 있는 request Servlet
     * @return null : Token이 존재하지 않는 경우 / Cookie : Token이 존재하는 경우
     */
    private Cookie findTokenCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    return cookie;
                }
            }
        }

        return null;
    }
}
