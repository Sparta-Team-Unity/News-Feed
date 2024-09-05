package com.sparta.newsfeed.config;

import com.sparta.newsfeed.domain.dto.UserDto;
import com.sparta.newsfeed.domain.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * AuthUser Annotation이 이미 존재하는지 확인
     * @param parameter MethodParameter 객체
     * @return true : 이미 AuthUser Annotation이 존재 / false : AuthUser Annotation이 존재하지 않음
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(AuthUser.class) != null;
    }

    /**
     * Argument에 들어갈 데이터를 조립해주는 메서드
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        assert request != null;

        User user = (User)request.getAttribute("user");
        UserDto userDto = UserDto.builder()
                .userEmail(user.getEmail())
                .username(user.getName())
                .id(user.getUserId())
                .build();

        return userDto;
    }
}
