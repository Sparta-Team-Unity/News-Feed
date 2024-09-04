package com.sparta.newsfeed.domain.service;

import com.sparta.newsfeed.domain.dto.FollowDto;
import com.sparta.newsfeed.domain.dto.FriendResponseDto;
import com.sparta.newsfeed.domain.entity.Friend;
import com.sparta.newsfeed.domain.exception.DuplicateFriendException;
import com.sparta.newsfeed.domain.repository.FriendRepository;
import com.sparta.newsfeed.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sparta.newsfeed.domain.entity.User;

import java.util.ArrayList;
import java.util.List;
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
        if(friendRepository.existsByToUserAndFromUser(toUser, fromUser)
                || friendRepository.existsByToUserAndFromUser(fromUser, toUser)){
            throw new DuplicateFriendException();
        }
        friendRepository.save(new Friend(toUser, fromUser));
    }

    public FriendResponseDto friendsInquiry() {

        // fromUser 추후에 JWT 에서 추출할 것
        User user = new User();
        List<Friend> friendList = friendRepository.findByFromUserAndIsAccepted(user,true);
        friendList.addAll(friendRepository.findByToUserAndIsAccepted(user,true));

        List<FollowDto> responseList = new ArrayList<>();

        for (Friend friend : friendList) {
            int friendId = friend.getFromUser().getUserId() == user.getUserId()
                    ? friend.getToUser().getUserId()
                    : friend.getFromUser().getUserId();

            responseList.add(new FollowDto(friendId));
        }
        return new FriendResponseDto(responseList);
    }
}
