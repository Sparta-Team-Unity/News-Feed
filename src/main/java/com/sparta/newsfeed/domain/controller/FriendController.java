package com.sparta.newsfeed.domain.controller;

import com.sparta.newsfeed.config.authconfig.AuthUser;
import com.sparta.newsfeed.domain.dto.friend.FriendResponseDto;
import com.sparta.newsfeed.domain.dto.user.UserDto;
import com.sparta.newsfeed.domain.dto.friend.WaitsResponseDto;
import com.sparta.newsfeed.domain.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follows")
public class FriendController {
    private final FriendService friendService;

    /**
     * 친구 신청 보내는 메서드
     * @param followId 상대 유저 Id
     * @param userDto 로그인된 유저 정보
     * @return 친구 요청 상태
     */
    @PostMapping("/{followId}")
    public ResponseEntity<Void> addFriend(@PathVariable("followId") Integer followId, @AuthUser UserDto userDto) {
        friendService.addFriend(followId, userDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{followId}").buildAndExpand(followId).toUri();
        return ResponseEntity.created(location).build();
    }

    /**
     * 친구 목록 조회
     * @param userDto 로그인된 유저 정보
     * @return 로그인된 유저의 현재 친구 목록
     */
    @GetMapping
    public ResponseEntity<FriendResponseDto> friendsInquiry(@AuthUser UserDto userDto){
        FriendResponseDto response = friendService.friendsInquiry(userDto);
        return ResponseEntity.ok(response);
    }

    /**
     * 요청 대기중인 친구 목록 조회
     * @param userDto 로그인된 유저 정보
     * @return 로그인된 유저에게 친구 요청온 목록
     */
    @GetMapping("/waits")
    public ResponseEntity<WaitsResponseDto> friendsInquiryWait(@AuthUser UserDto userDto){
        WaitsResponseDto response = friendService.waitsInquiry(userDto);
        return ResponseEntity.ok(response);
    }

    /**
     * 친구 요청을 수락하는 메서드
     * @param followId 상대 유저 Id
     * @param userDto 로그인된 유저 정보
     * @return 수락 상태
     */
    @PutMapping("/{followId}")
    public ResponseEntity<Void> reciveFriend(@PathVariable("followId") Integer followId, @AuthUser UserDto userDto) {
        friendService.reciveFriend(followId,userDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 친구 삭제하는 메서드
     * @param followId 상대 유저 Id
     * @param userDto 로그인된 유저 정보
     * @return 삭제 상태
     */
    @DeleteMapping("/{followId}")
    public ResponseEntity<Void> deleteFriend(@PathVariable("followId") Integer followId, @AuthUser UserDto userDto) {
        friendService.deleteFriend(followId, userDto);
        return ResponseEntity.ok().build();
    }

}
