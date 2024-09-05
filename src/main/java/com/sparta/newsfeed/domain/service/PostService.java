package com.sparta.newsfeed.domain.service;

import com.sparta.newsfeed.domain.dto.FollowDto;
import com.sparta.newsfeed.domain.dto.PostRequestDto;
import com.sparta.newsfeed.domain.dto.PostResponseDto;
import com.sparta.newsfeed.domain.dto.PostUpdateRequestDto;
import com.sparta.newsfeed.domain.entity.Friend;
import com.sparta.newsfeed.domain.entity.Post;
import com.sparta.newsfeed.domain.entity.User;
import com.sparta.newsfeed.domain.exception.PostNotExistException;
import com.sparta.newsfeed.domain.exception.UserNotExistException;
import com.sparta.newsfeed.domain.repository.FriendRepository;
import com.sparta.newsfeed.domain.repository.PostRepository;
import com.sparta.newsfeed.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto) {
        // 해당 유저가 존재하는지 확인 (필요한지는 모르겠어요... 로그인 상태이면 필요 x)
        User user = userRepository.findById(postRequestDto.getUserId()).orElseThrow(() -> new NoSuchElementException("User not found"));

        Post newPost = new Post(postRequestDto.getTitle(), postRequestDto.getContents(), user);
        Post savedPost = postRepository.save(newPost);

        return new PostResponseDto(
                savedPost.getId(),
                savedPost.getTitle(),
                savedPost.getContents(),
                savedPost.getCreateAt(),
                savedPost.getEditAt(),
                savedPost.getUser()
        );
    }

    public Page<PostResponseDto> getAllPosts(int page, int size, int userId) {
        Pageable pageable = PageRequest.of(page - 1, size);

        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("User not found"));

        List<User> userList = new ArrayList<>();

        List<Friend> friendList = friendRepository.findByFromUserAndIsAccepted(user, true);
        log.info("friendList: {}", friendList);
        friendList.addAll(friendRepository.findByToUserAndIsAccepted(user, true));
        for (Friend friend : friendList) {
            User friendUser = friend.getFromUser().equals(user)
                    ? friend.getToUser()
                    : friend.getFromUser();

            userList.add(friendUser);
        }
        userList.add(user);

        return postRepository.findByUserInOrderByCreateAtDesc(userList, pageable)
                .map(post -> PostResponseDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .contents(post.getContents())
                        .createAt(post.getCreateAt())
                        .editAt(post.getEditAt())
                        .user(post.getUser())
                        .build()
                );
    }

    @Transactional
    public void updatePost(Integer postId, PostUpdateRequestDto postUpdateRequestDto) {
        // 해당 post가 존재하는지 확인
        Post post = postRepository.findById(postId).orElseThrow(PostNotExistException::new);

        // 해당 user 존재하는지 확인
        User user = userRepository.findById(postUpdateRequestDto.getUserId()).orElseThrow(UserNotExistException::new);

        if (post.getUser() == null || !user.getUserId().equals(post.getUser().getUserId())) {
            throw new NoSuchElementException("작성자가 일치하지 않습니다.");
        }

        post.update(
                postUpdateRequestDto.getTitle(),
                postUpdateRequestDto.getContents()
        );

    }

    @Transactional
    public void deletePost(Integer postId) {
        // 해당 post가 존재하는지 확인
        Post post = postRepository.findById(postId).orElseThrow(PostNotExistException::new);

        // 작성자와 현재 계정이 일치한지 확인 필요

        // user 확인 어떻게 하지??
        // 해당 user 존재하는지 확인
//        User user = userRepository.findById(postUpdateRequestDto.getUserId()).orElseThrow(()-> new NoSuchElementException("User not found"));
//
//        if(post.getUser() == null || !ObjectUtils.nullSafeEquals(user.getUserId(), post.getUser().getUserId())) {
//            throw new NoSuchElementException("작성자가 일치하지 않습니다.");
//        }

        postRepository.delete(post);
    }
}
