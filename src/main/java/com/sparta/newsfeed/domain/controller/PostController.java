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

    /**
     * 게시물 작성
     * @param postRequestDto
     * @return
     */
    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto postRequestDto){
        return ResponseEntity.ok(postService.createPost(postRequestDto));
    }

    /**
     * 본인, 친구 게시물 전체 조회
     * @param page
     * @param size
     * @param userId
     * @return
     */
    @GetMapping("/posts")
    public ResponseEntity<Page<PostResponseDto>> getAllPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam int userId
    ){
        return ResponseEntity.ok(postService.getAllPosts(page,size,userId));
    }

    /**
     * 게시글 수정
     * @param postId
     * @param postUpdateRequestDto
     * @return
     */
    @PutMapping("/posts/{postId}")
    public ResponseEntity<Void> updatePost(@PathVariable("postId") Integer postId, @RequestBody PostUpdateRequestDto postUpdateRequestDto){
        postService.updatePost(postId, postUpdateRequestDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 게시글 삭제
     * @param postId
     * @return
     */
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable("postId") Integer postId){
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }
}
