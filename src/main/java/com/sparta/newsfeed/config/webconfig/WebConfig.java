package com.sparta.newsfeed.config.webconfig;

import com.sparta.newsfeed.config.authconfig.AuthUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * ArgumentResolver등록하는 메서드
     * @param resolvers AuthUserArgumentResolver를 담을 핸들러 컨테이너
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthUserArgumentResolver());
    }
}
