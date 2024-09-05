package com.sparta.newsfeed.domain.controller;

import com.sparta.newsfeed.domain.dto.FriendResponseDto;
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

    @PostMapping("/{followid}")
    public ResponseEntity<Void> addFriend(@PathVariable("followid") Integer followid) {
        friendService.addFriend(followid);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<FriendResponseDto> friendsInquiry(){
        FriendResponseDto response = friendService.friendsInquiry();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/waits")
    public ResponseEntity<WaitsResponseDto> friendsInquiryWait(){
        WaitsResponseDto response = friendService.waitsInquiry();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{followid}")
    public ResponseEntity<Void> reciveFriend(@PathVariable("followid") Integer followid) {
        friendService.reciveFriend(followid);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{followid}")
    public ResponseEntity<Void> deleteFriend(@PathVariable("followid") Integer followid) {
        friendService.deleteFriend(followid);
        return ResponseEntity.ok().build();
    }

}
