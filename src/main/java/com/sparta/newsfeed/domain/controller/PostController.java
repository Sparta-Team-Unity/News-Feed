package com.sparta.newsfeed.domain.controller;

import com.sparta.newsfeed.config.AuthUser;
import com.sparta.newsfeed.domain.dto.*;
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

    /**
     * 게시물 작성
     * @param postRequestDto
     * @return
     */
    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto postRequestDto, @AuthUser UserDto userDto){
        return ResponseEntity.ok(postService.createPost(postRequestDto, userDto));
    }

    /**
     * 본인, 친구 게시물 전체 조회
     * @param page
     * @param size
     * @param userDto
     * @return
     */
    @GetMapping("/posts")
    public ResponseEntity<Page<PostResponseDto>> getAllPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthUser UserDto userDto
    ){
        return ResponseEntity.ok(postService.getAllPosts(page,size,userDto));
    }

    /**
     * 게시글 수정
     * @param postId
     * @param postUpdateRequestDto
     * @param userDto
     * @return
     */
    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostUpdateResponseDto> updatePost(
            @PathVariable("postId") Integer postId,
            @RequestBody PostUpdateRequestDto postUpdateRequestDto,
            @AuthUser UserDto userDto
    ){
        return ResponseEntity.ok(postService.updatePost(postId, postUpdateRequestDto, userDto));
    }

    /**
     * 게시글 삭제
     * @param postId
     * @param userDto
     * @return
     */
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable("postId") Integer postId,
            @AuthUser UserDto userDto
    ){
        postService.deletePost(postId, userDto);
        return ResponseEntity.ok().build();
    }
}
