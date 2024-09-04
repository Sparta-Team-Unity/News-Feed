package com.sparta.newsfeed.domain.service;

import com.sparta.newsfeed.domain.entity.Friend;
import com.sparta.newsfeed.domain.exception.DuplicateFriendException;
import com.sparta.newsfeed.domain.repository.FriendRepository;
import com.sparta.newsfeed.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sparta.newsfeed.domain.entity.User;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    public void addFriend(Integer followid) {
        User toUser = userRepository.findById(followid).orElseThrow(()->new NoSuchElementException("User Not Found"));

        // fromUser 추후에 JWT 에서 추출할 것
        User fromUser = new User();
        if(friendRepository.existsByToUserIdAndFromUserId(toUser.getUserId(), 1)
                || friendRepository.existsByToUserIdAndFromUserId(1, toUser.getUserId())){
            throw new DuplicateFriendException();
        }
        friendRepository.save(new Friend(toUser, fromUser));
    }
}
