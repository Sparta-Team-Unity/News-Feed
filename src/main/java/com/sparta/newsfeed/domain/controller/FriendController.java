package com.sparta.newsfeed.domain.controller;

import com.sparta.newsfeed.config.AuthUser;
import com.sparta.newsfeed.domain.dto.FriendResponseDto;
import com.sparta.newsfeed.domain.dto.UserDto;
import com.sparta.newsfeed.domain.dto.WaitsResponseDto;
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

    //친구 신청
    @PostMapping("/{followId}")
    public ResponseEntity<Void> addFriend(@PathVariable("followId") Integer followId, @AuthUser UserDto userDto) {
        friendService.addFriend(followId, userDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{followId}").buildAndExpand(followId).toUri();
        return ResponseEntity.created(location).build();
    }

    //친구목록조회
    @GetMapping
    public ResponseEntity<FriendResponseDto> friendsInquiry(@AuthUser UserDto userDto){
        FriendResponseDto response = friendService.friendsInquiry(userDto);
        return ResponseEntity.ok(response);
    }

    //친구신청대기 목록 조회
    @GetMapping("/waits")
    public ResponseEntity<WaitsResponseDto> friendsInquiryWait(@AuthUser UserDto userDto){
        WaitsResponseDto response = friendService.waitsInquiry(userDto);
        return ResponseEntity.ok(response);
    }

    //친구 수락
    @PutMapping("/{followId}")
    public ResponseEntity<Void> reciveFriend(@PathVariable("followId") Integer followId, @AuthUser UserDto userDto) {
        friendService.reciveFriend(followId,userDto);
        return ResponseEntity.ok().build();
    }

    //친구삭제
    @DeleteMapping("/{followId}")
    public ResponseEntity<Void> deleteFriend(@PathVariable("followId") Integer followId, @AuthUser UserDto userDto) {
        friendService.deleteFriend(followId, userDto);
        return ResponseEntity.ok().build();
    }

}
