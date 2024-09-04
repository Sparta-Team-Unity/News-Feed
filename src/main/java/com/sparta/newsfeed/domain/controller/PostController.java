package com.sparta.newsfeed.domain.controller;

import com.sparta.newsfeed.domain.dto.PostUpdateRequestDto;
import com.sparta.newsfeed.domain.dto.PostRequestDto;
import com.sparta.newsfeed.domain.dto.PostResponseDto;
import com.sparta.newsfeed.domain.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto postRequestDto){
        postService.createPost(postRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/posts")
    public ResponseEntity<Page<PostResponseDto>> getAllPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(postService.getAllPosts(page,size));
    }


    @PutMapping("/posts/{postId}")
    public ResponseEntity<Void> updatePost(@PathVariable("postId") Integer id, @RequestBody PostUpdateRequestDto postUpdateRequestDto){

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/posts/{postId}"){

    }
}
