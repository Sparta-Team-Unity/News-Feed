package com.sparta.newsfeed.domain.controller;

import com.sparta.newsfeed.config.AuthUser;
import com.sparta.newsfeed.domain.dto.UserDto;
import com.sparta.newsfeed.domain.dto.UserLoginRequestDto;
import com.sparta.newsfeed.domain.dto.UserSignUpRequestDto;
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

    @PostMapping("/users")
    public void signUp(@RequestBody UserSignUpRequestDto userSignupRequestDto) {
        userService.signUp(userSignupRequestDto);
    }

    @DeleteMapping("/users")
    public void signOut(@AuthUser UserDto user) throws Exception {
        userService.signOut(user);
    }

    @PostMapping("/users/login")
    public String logIn(@RequestBody UserLoginRequestDto userLoginRequestDto, HttpServletResponse response) {
        return userService.login(userLoginRequestDto, response);
    }

    @DeleteMapping("/users/logout")
    public void logOut(@AuthUser UserDto user) throws Exception {
        userService.logout(user);
    }

    /**
     * request에 들어있는 User사용법
     * @param user 유저 정보가 있는 Dto
     * @return string화 된 user
     */
    @DeleteMapping("/users/test")
    public String test(@AuthUser UserDto user) {

        return user.toString();
    }
}
