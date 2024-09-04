package com.sparta.newsfeed.domain.controller;

import com.sparta.newsfeed.domain.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/follows")
public class FriendController {
//    private final FriendService friendService;

    @PostMapping("/{followid}")
    public ResponseEntity<Void> addFriend(@PathVariable("followid") Integer followid) {
//        friendService.addFriend(followid);
        return ResponseEntity.ok().build();
    }


}
