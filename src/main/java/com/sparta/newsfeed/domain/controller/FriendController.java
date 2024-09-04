package com.sparta.newsfeed.domain.controller;

import com.sparta.newsfeed.domain.dto.FriendResponseDto;
import com.sparta.newsfeed.domain.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follows")
public class FriendController {
    private final FriendService friendService;

    @PostMapping("{/followid}")
    public ResponseEntity<Void> addFriend(@PathVariable("followid") Integer followid) {
        friendService.addFriend(followid);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<FriendResponseDto> friendsInquiry(){
        FriendResponseDto response = friendService.friendsInquiry();
        return ResponseEntity.ok(response);
    }


}
