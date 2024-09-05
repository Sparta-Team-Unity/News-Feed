package com.sparta.newsfeed.domain.controller;

import com.sparta.newsfeed.config.AuthUser;
import com.sparta.newsfeed.domain.dto.UserDto;
import com.sparta.newsfeed.domain.dto.UserRequestDto;
import com.sparta.newsfeed.domain.entity.User;
import com.sparta.newsfeed.domain.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
    public void signUp(@RequestBody UserRequestDto userRequestDto) {
        userService.signUp(userRequestDto);
    }

    @PostMapping("/users/login")
    public String logIn(@RequestBody UserRequestDto userRequestDto, HttpServletResponse response) {
        return userService.login(userRequestDto, response);
    }

    @DeleteMapping("/users/logout")
    public void logOut(HttpServletRequest request, HttpServletResponse response) {
        userService.logout(request, response);
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
