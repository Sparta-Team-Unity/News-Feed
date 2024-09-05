package com.sparta.newsfeed.config;

import com.sparta.newsfeed.domain.repository.UserRepository;
import com.sparta.newsfeed.domain.service.BlacklistTokenService;
import com.sparta.newsfeed.domain.service.TokenService;
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
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final BlacklistTokenService blacklistTokenService;

    public FilterConfig(JwtUtil jwtUtil,
                        UserRepository userRepository,
                        TokenService tokenService,
                        BlacklistTokenService blacklistTokenService) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
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
        filterRegistrationBean.setFilter(new JwtFilter(jwtUtil, userRepository, tokenService, blacklistTokenService));
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
                        // 우선 모든 URL에 대한 접근이 가능하게 생성
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        // 그 외에는 인증이 되어야 한다.
                        .anyRequest().authenticated()
        );

        return http.build();
    }

}
