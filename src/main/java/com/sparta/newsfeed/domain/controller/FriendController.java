package com.sparta.newsfeed.domain.controller;

import com.sparta.newsfeed.config.AuthUser;
import com.sparta.newsfeed.domain.dto.FriendResponseDto;
import com.sparta.newsfeed.domain.dto.UserDto;
import com.sparta.newsfeed.domain.dto.WaitsResponseDto;
import com.sparta.newsfeed.domain.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follows")
public class FriendController {
    private final FriendService friendService;

    //친구 신청
    @PostMapping("/{followid}")
    public ResponseEntity<Void> addFriend(@PathVariable("followid") Integer followid, @AuthUser UserDto userDto) {
        friendService.addFriend(followid, userDto);
        return ResponseEntity.ok().build();
    }

    //친구목록조회
    @GetMapping
    public ResponseEntity<FriendResponseDto> friendsInquiry(@AuthUser UserDto userDto){
        System.out.println("요청 제대로 들어옴");
        FriendResponseDto response = friendService.friendsInquiry(userDto);
        System.out.println("서비스 나왔어");
        return ResponseEntity.ok(response);
    }

    //친구신청대기 목록 조회
    @GetMapping("/waits")
    public ResponseEntity<WaitsResponseDto> friendsInquiryWait(@AuthUser UserDto userDto){
        WaitsResponseDto response = friendService.waitsInquiry(userDto);
        return ResponseEntity.ok(response);
    }

    //친구 수락
    @PutMapping("/{followid}")
    public ResponseEntity<Void> reciveFriend(@PathVariable("followid") Integer followid, @AuthUser UserDto userDto) {
        friendService.reciveFriend(followid,userDto);
        return ResponseEntity.ok().build();
    }

    //친구삭제
    @DeleteMapping("/{followid}")
    public ResponseEntity<Void> deleteFriend(@PathVariable("followid") Integer followid, @AuthUser UserDto userDto) {
        friendService.deleteFriend(followid, userDto);
        return ResponseEntity.ok().build();
    }

}
