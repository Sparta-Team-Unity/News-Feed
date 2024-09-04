package com.sparta.newsfeed.domain.service;

import com.sparta.newsfeed.domain.dto.PostRequestDto;
import com.sparta.newsfeed.domain.dto.PostResponseDto;
import com.sparta.newsfeed.domain.entity.Post;
import com.sparta.newsfeed.domain.entity.User;
import com.sparta.newsfeed.domain.repository.PostRepository;
import com.sparta.newsfeed.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostResponseDto createPost(PostRequestDto postRequestDto) {
        // 해당 유저가 존재하는지 확인 (필요한지는 모르겠어요... 로그인 상태이면 필요 x)
        User user = userRepository.findByUserId(postRequestDto.getUserId()).orElseThrow(()-> new NoSuchElementException("User not found"));

        Post newPost = new Post(postRequestDto.getTitle(), postRequestDto.getContents(),user);
        Post savedPost = postRepository.save(newPost);

        return null;
    }

    public Page<PostResponseDto> getAllPosts(int page, int size) {
        return null;
    }
}
