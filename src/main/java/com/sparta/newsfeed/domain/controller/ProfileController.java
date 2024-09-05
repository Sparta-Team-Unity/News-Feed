package com.sparta.newsfeed.domain.controller;

import com.sparta.newsfeed.config.AuthUser;
import com.sparta.newsfeed.domain.dto.ProfileSaveRequestDto;
import com.sparta.newsfeed.domain.dto.ProfileViewResponseDto;
import com.sparta.newsfeed.domain.dto.UserDto;
import com.sparta.newsfeed.domain.dto.UserRequestDto;
import com.sparta.newsfeed.domain.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RequestMapping("/api/profiles")
@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{userId}")
    public ResponseEntity<ProfileViewResponseDto> viewProfile(@PathVariable Integer userId, @AuthUser UserDto userDto) {
        return ResponseEntity.ok(profileService.viewUsersProfile(userId, userDto));
    }

    @PutMapping
    public ResponseEntity<Void> postProfile(@RequestBody ProfileSaveRequestDto profileRequestdto, @AuthUser UserDto userDto) {
        profileService.updateProfile(profileRequestdto, userDto);
        return ResponseEntity.ok().build();
    }
}
