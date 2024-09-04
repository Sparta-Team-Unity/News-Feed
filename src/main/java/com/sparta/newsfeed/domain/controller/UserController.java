package com.sparta.newsfeed.domain.controller;

import com.sparta.newsfeed.config.JwtUtil;
import com.sparta.newsfeed.domain.dto.UserRequestDto;
import com.sparta.newsfeed.domain.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(JwtUtil jwtUtil, UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public void signUp(@RequestBody UserRequestDto userRequestDto) {
        userService.signUp(userRequestDto);
    }

    @GetMapping("/users/login")
    public String logIn(@RequestBody UserRequestDto userRequestDto) {
        return userService.login(userRequestDto);
    }
}
