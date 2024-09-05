package com.sparta.newsfeed.domain.controller;

import com.sparta.newsfeed.config.authconfig.AuthUser;
import com.sparta.newsfeed.domain.dto.profile.ProfileSaveRequestDto;
import com.sparta.newsfeed.domain.dto.profile.ProfileViewResponseDto;
import com.sparta.newsfeed.domain.dto.user.UserDto;
import com.sparta.newsfeed.domain.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/profiles")
@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    /**
     * 해당 유저 프로필을 조회하는 메서드
     * @param userId 조회할 유저 Id
     * @param userDto 로그인 중인 유저 정보
     * @return 해당 유저의 개인 정보
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ProfileViewResponseDto> viewProfile(@PathVariable Integer userId, @AuthUser UserDto userDto) {
        return ResponseEntity.ok(profileService.viewUsersProfile(userId, userDto));
    }

    /**
     * 프로필을 변경하는 메서드
     * @param profileRequestdto 변경할 프로필 내용
     * @param userDto 로그인된 유저 상태
     * @return 프로필 변경 상태
     */
    @PutMapping
    public ResponseEntity<Void> postProfile(@RequestBody ProfileSaveRequestDto profileRequestdto, @AuthUser UserDto userDto) {
        profileService.updateProfile(profileRequestdto, userDto);
        return ResponseEntity.ok().build();
    }
}
