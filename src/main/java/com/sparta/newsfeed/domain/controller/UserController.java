package com.sparta.newsfeed.domain.controller;

import com.sparta.newsfeed.config.authconfig.AuthUser;
import com.sparta.newsfeed.domain.dto.user.UserDto;
import com.sparta.newsfeed.domain.dto.user.UserLoginRequestDto;
import com.sparta.newsfeed.domain.dto.user.UserSignUpRequestDto;
import com.sparta.newsfeed.domain.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 회원가입 메서드
     * @param userSignupRequestDto 회원가입시 기재한 내용
     */
    @PostMapping("/users")
    public void signUp(@RequestBody UserSignUpRequestDto userSignupRequestDto) {
        userService.signUp(userSignupRequestDto);
    }

    /**
     * 회원 탈퇴하는 메서드
     * @param user 로그인된 유저 정보
     */
    @DeleteMapping("/users")
    public void signOut(@AuthUser UserDto user) {
        userService.signOut(user);
    }

    /**
     * 로그인하는 메서드
     * @param userLoginRequestDto 로그인에 필요한 유저 정보
     * @param response 토큰을 담을 HttpServletResponse 객체
     * @return Access 토큰
     */
    @PostMapping("/users/login")
    public String logIn(@RequestBody UserLoginRequestDto userLoginRequestDto, HttpServletResponse response) {
        return userService.login(userLoginRequestDto, response);
    }

    /**
     * 로그아웃하는 메서드
     * @param user 현재 로그인 중인 유저 정보
     */
    @DeleteMapping("/users/logout")
    public void logOut(@AuthUser UserDto user) {
        userService.logout(user);
    }
}
