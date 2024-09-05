package com.sparta.newsfeed.domain.controller;

import com.sparta.newsfeed.config.authconfig.AuthUser;
import com.sparta.newsfeed.domain.dto.post.PostRequestDto;
import com.sparta.newsfeed.domain.dto.post.PostResponseDto;
import com.sparta.newsfeed.domain.dto.post.PostUpdateRequestDto;
import com.sparta.newsfeed.domain.dto.post.PostUpdateResponseDto;
import com.sparta.newsfeed.domain.dto.user.UserDto;
import com.sparta.newsfeed.domain.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    /**
     * 게시물 작성
     * @param postRequestDto 게시글 정보가 담긴 객체
     * @return 게시글 작성 상태
     */
    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto postRequestDto, @AuthUser UserDto userDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(postRequestDto, userDto));
    }


    /**
     * 본인, 친구 게시물 전체 조회
     * @param page 조회할 페이지
     * @param size 페이지 크기
     * @param userDto 현재 로그인 중인 유저 정보
     * @return 본인, 친구 전체 게시물 중 해당 페이지 내용
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
     * @param postId 게시글 Id
     * @param postUpdateRequestDto 수정할 게시글 내용
     * @param userDto 로그인 중인 유저 정보
     * @return 수정된 게시글 내용
     */
    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostUpdateResponseDto> updatePost(
            @PathVariable("postId") Integer postId,
            @RequestBody PostUpdateRequestDto postUpdateRequestDto,
            @AuthUser UserDto userDto
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.updatePost(postId, postUpdateRequestDto, userDto));
    }

    /**
     * 게시글 삭제
     * @param postId 게시글 Id
     * @param userDto 로그인 중인 유저 정보
     * @return 삭제 상태
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
