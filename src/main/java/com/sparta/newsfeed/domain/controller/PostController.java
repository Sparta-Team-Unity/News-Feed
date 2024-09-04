package com.sparta.newsfeed.domain.controller;

import com.sparta.newsfeed.domain.dto.request.PostRequestDto;
import com.sparta.newsfeed.domain.dto.response.PostResponseDto;
import com.sparta.newsfeed.domain.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto postRequestDto){
        return ResponseEntity.ok(postService.createPost(postRequestDto));
    }

}
