package com.sparta.newsfeed.domain.service;

import com.sparta.newsfeed.domain.dto.post.PostRequestDto;
import com.sparta.newsfeed.domain.dto.post.PostResponseDto;
import com.sparta.newsfeed.domain.dto.post.PostUpdateRequestDto;
import com.sparta.newsfeed.domain.dto.post.PostUpdateResponseDto;
import com.sparta.newsfeed.domain.dto.user.UserDto;
import com.sparta.newsfeed.domain.entity.Friend;
import com.sparta.newsfeed.domain.entity.Post;
import com.sparta.newsfeed.domain.entity.User;
import com.sparta.newsfeed.domain.exception.ErrorCode;
import com.sparta.newsfeed.domain.exception.UnityException;
import com.sparta.newsfeed.domain.repository.FriendRepository;
import com.sparta.newsfeed.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final FriendRepository friendRepository;

    /**
     * 게시글 생성해주는 메서드
     * @param postRequestDto 게시글 작성 내용
     * @param userDto 로그인 중인 유저 정보
     * @return 작성된 게시글 정보
     */
    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, UserDto userDto) {
        // 해당 유저가 존재하는지 확인
        User user = findUserById(userDto.getId());

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

    /**
     * 게시글 조회 (자기자신 + 친구 게시글 조회)
     * @param page 조회할 페이지
     * @param size 페이지 크기
     * @param userDto 현재 로그인 중인 유저 정보
     * @return 본인, 친구 전체 게시물 중 해당 페이지 내용
     */
    public Page<PostResponseDto> getAllPosts(int page, int size, UserDto userDto) {
        Pageable pageable = PageRequest.of(page - 1, size);

        // 해당 user가 존재하는지 확인
        User user = findUserById(userDto.getId());

        List<User> userList = new ArrayList<>();

        List<Friend> friendList = friendRepository.findByFromUserAndIsAccepted(user, true);
        log.info("friendList: {}", friendList);
        friendList.addAll(friendRepository.findByToUserAndIsAccepted(user, true));
        log.info("friendList To User: {}", friendList);
        for (Friend friend : friendList) {
            User friendUser = friend.getFromUser().equals(user)
                    ? friend.getToUser()
                    : friend.getFromUser();

            userList.add(friendUser);
        }
        log.info("userList: {}", userList);
        userList.add(user);

        Page<Post> postPage = postRepository.findByUserInOrderByCreateAtDesc(userList, pageable)
                .orElseThrow(()-> new UnityException(ErrorCode.POST_NOT_FOUND));
        if(postPage.isEmpty()){
            throw new UnityException(ErrorCode.POST_NOT_FOUND);
        }

        return postPage
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

    /**
     * 게시글 수정 메서드
     * @param postId 수정할 게시글 Id
     * @param postUpdateRequestDto 수정할 게시글 내용
     * @param userDto 로그인된 유저 정보
     * @return 수정된 게시글 내용
     */
    @Transactional
    public PostUpdateResponseDto updatePost(Integer postId, PostUpdateRequestDto postUpdateRequestDto, UserDto userDto) {
        // 해당 post와 user 존재여부 확인 및 게시글 사용자 인증
        Post post = postUserAuthentication(userDto.getId(), postId);

        post.update(
                postUpdateRequestDto.getTitle(),
                postUpdateRequestDto.getContents()
        );

        return PostUpdateResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())
                .createAt(post.getCreateAt())
                .editAt(post.getEditAt())
                .user(post.getUser())
                .build();

    }

    /**
     * 게시글 삭제하는 메서드
     * @param postId 삭제할 게시글 Id
     * @param userDto 로그인된 유저 정보
     */
    @Transactional
    public void deletePost(Integer postId, UserDto userDto) {
        // 해당 post와 user 존재여부 확인 및 게시글 사용자 인증
        Post post = postUserAuthentication(userDto.getId(), postId);

        postRepository.delete(post);
    }


    /**
     * postId로 게시글을 조회하는 메서드
     * @param postId 조회할 게시글 Id
     * @return 조회한 게시글
     */
    private Post findPostById(Integer postId) {
        return postRepository.findById(postId).orElseThrow(()->new UnityException(ErrorCode.POST_NOT_EXIST));
    }

    /**
     * userId로 User를 조회하는 메서드
     * @param userId 조회할 유저 Id
     * @return 조회한 유저
     */
    private User findUserById(Integer userId) {
        return userService.findUserById(userId);
    }

    /**
     * 해당 Post와 User가 존재하는지 여부 & 작성자 인증
     * @param userId 유저 Id
     * @param postId 게시글 Id
     * @return 게시글 내용
     */
    private Post postUserAuthentication(Integer userId, Integer postId){
        // 해당 user 존재하는지 확인
        User user = findUserById(userId);

        // 해당 post가 존재하는지 확인
        Post post = findPostById(postId);

        // post 작성자와 현재 login 사용자 id 일치 여부 확인
        if (post.getUser() == null || !user.getUserId().equals(post.getUser().getUserId())) {
            throw new UnityException(ErrorCode.USER_NOT_EXIST);
        }

        return post;
    }


}
