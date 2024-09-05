package com.sparta.newsfeed.config.filter;

import com.sparta.newsfeed.domain.repository.UserRepository;
import com.sparta.newsfeed.domain.service.BlacklistTokenService;
import com.sparta.newsfeed.domain.service.TokenService;
import com.sparta.newsfeed.domain.service.UserService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class FilterConfig {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final TokenService tokenService;
    private final BlacklistTokenService blacklistTokenService;

    public FilterConfig(JwtUtil jwtUtil,
                        UserService userRepository,
                        TokenService tokenService,
                        BlacklistTokenService blacklistTokenService) {
        this.jwtUtil = jwtUtil;
        this.userService = userRepository;
        this.tokenService = tokenService;
        this.blacklistTokenService = blacklistTokenService;
    }

    /**
     * 필터 등록하는 메서드
     * @return 필터가 등록된 RegistrationBean
     */
    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterRegistration() {
        FilterRegistrationBean<JwtFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new JwtFilter(jwtUtil, userService, tokenService, blacklistTokenService));
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }

    /**
     * URL을 필터링 하는 메서드
     * @param http http 정보가 담긴 객체
     * @return URL 필터가 적용된 SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((AbstractHttpConfigurer::disable));

        // 접근 권한 설정
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        // 유저 관련 승인
                        .requestMatchers("/api/users").permitAll()
                        .requestMatchers("/api/users/login").permitAll()
                        .requestMatchers("/api/users/logout").permitAll()
                        // 친구 요청 관련
                        .requestMatchers("/api/follows").permitAll()
                        .requestMatchers("/api/follows/").permitAll()
                        .requestMatchers("/api/follows/waits").permitAll()
                        // 포스트 관련
                        .requestMatchers("/api/posts").permitAll()
                        .requestMatchers("/api/posts/").permitAll()
                        // 프로필 관련
                        .requestMatchers("/api/profiles").permitAll()
                        .requestMatchers("/api/profiles/").permitAll()
                        // 에러 관련
                        .requestMatchers("/error").permitAll()
                        // 그 외에는 인증이 되어야 한다.
                        .anyRequest().authenticated()
        );

        return http.build();
    }

}
