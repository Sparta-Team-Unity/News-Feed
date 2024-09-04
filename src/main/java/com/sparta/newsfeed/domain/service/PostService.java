package com.sparta.newsfeed.domain.service;

import com.sparta.newsfeed.domain.dto.request.PostRequestDto;
import com.sparta.newsfeed.domain.dto.response.PostResponseDto;
import com.sparta.newsfeed.domain.repository.PostRepository;
import com.sparta.newsfeed.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostResponseDto createPost(PostRequestDto postRequestDto) {
    }
}
