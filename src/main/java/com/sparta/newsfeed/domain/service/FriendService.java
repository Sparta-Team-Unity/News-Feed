package com.sparta.newsfeed.domain.service;

import com.sparta.newsfeed.domain.dto.*;
import com.sparta.newsfeed.domain.entity.Friend;
import com.sparta.newsfeed.domain.exception.AlreadyFriend;
import com.sparta.newsfeed.domain.exception.DuplicateFriendException;
import com.sparta.newsfeed.domain.exception.NoFriend;
import com.sparta.newsfeed.domain.exception.SamePersonException;
import com.sparta.newsfeed.domain.repository.FriendRepository;
import com.sparta.newsfeed.domain.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sparta.newsfeed.domain.entity.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
@Transactional
public class FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    public void addFriend(Integer followid, UserDto userDto) {
        User toUser = userRepository.findById(followid).orElseThrow(()->new NoSuchElementException("User Not Found"));
        User currentUser = currentUser(userDto);
        if(toUser.equals(currentUser)) {
            throw new SamePersonException();
        }
        else {
            if(friendRepository.existsByToUserAndFromUser(toUser, currentUser)
                    || friendRepository.existsByToUserAndFromUser(currentUser, toUser)){
                throw new DuplicateFriendException();
            }
            friendRepository.save(new Friend(toUser, currentUser));
        }
    }

    @Transactional(readOnly = true)
    public FriendResponseDto friendsInquiry(UserDto userDto) {

        // user -> 로그인 중인 현재 사용자
        User currentUser = currentUser(userDto);
        //friendList는 친구 신청을 하거나 받은 사람중 수락된 Friend 객체들만 전부 모은 리스트
        List<Friend> friendList = friendRepository.findByFromUserAndIsAccepted(currentUser,true);
        friendList.addAll(friendRepository.findByToUserAndIsAccepted(currentUser,true));

        List<FollowDto> responseList = new ArrayList<>();
        // friendList 순회 -> FollowDto 객체들을 만들어 responseList 갱신
        for (Friend friend : friendList) {
            int friendId = friend.getFromUser().getUserId() == currentUser.getUserId()
                    ? friend.getToUser().getUserId()
                    : friend.getFromUser().getUserId();

            responseList.add(new FollowDto(friendId));
        }
        return new FriendResponseDto(responseList);
    }

    @Transactional(readOnly = true)
    public WaitsResponseDto waitsInquiry(UserDto userDto) {


        // user -> 로그인 중인 현재 사용자
        User currentUser = currentUser(userDto);
        // 수락 대기중인 Friend 객체 담은 wiatList 리스트 순회 waitsDto 담은 responseList 리스트 갱신
        List<Friend> wiatList = friendRepository.findByToUserAndIsAccepted(currentUser,false);

        List<WaitsDto> responseList = new ArrayList<>();

        for (Friend wait : wiatList) {
            int waitsId = wait.getFromUser().getUserId();
            LocalDateTime time = wait.getCreateAt();
            responseList.add(new WaitsDto(waitsId,time));
        }
        return new WaitsResponseDto(responseList);
    }

    public void reciveFriend(Integer followid, UserDto userDto) {

        // fromUser 추후에 JWT 에서 추출할 것
        // user -> 로그인 중인 현재 사용자
        User currentUser = currentUser(userDto);
        //fromUser -> 친구 요청을 보낸 유저
        User fromUser = userRepository.findById(followid).orElseThrow(()->new NoSuchElementException("User Not Found"));

        // fromUser가 user에게 보낸 친구 요청 있는지 확인
        // 이미 친구 관계인지 확인
        if(friendRepository.existsByToUserAndFromUser(currentUser, fromUser)){
            if(friendRepository.existsByToUserAndFromUserAndIsAccepted(currentUser, fromUser, false)){
                // 수락 대기중인 친구 요청 찾았을 때 -> 수락 상태로 변화
                friendRepository.updateFriendRequestStatus(fromUser,currentUser,true);
            }
            else {
                //이미 친구가 된 요청 찾았을 때
                throw new AlreadyFriend();
            }
        }else {
            // 친구 요청 찾을 수 없을 때
            throw new NoSuchElementException("Can Not Found Friend Request");
        }
    }


    public void deleteFriend(Integer followid, UserDto userDto) {
        // fromUser 추후에 JWT 에서 추출할 것
        // user -> 로그인 중인 현재 사용자
        User currentUser = currentUser(userDto);
        User targetUser = userRepository.findById(followid).orElseThrow(()->new NoSuchElementException("User Not Found"));

        //친구 관계인지 확인
        //메서드 따로 빼기

        if(friendRepository.existsByToUserAndFromUserAndIsAccepted(currentUser, targetUser, true)) {

            //친구 일 때
            Friend friend = friendRepository.findByToUserAndFromUserAndIsAccepted(currentUser,targetUser,true);
            friendRepository.delete(friend);
        } else if (friendRepository.existsByToUserAndFromUserAndIsAccepted(targetUser,currentUser, true)) {
            // 친구 일 때
            Friend friend = friendRepository.findByToUserAndFromUserAndIsAccepted(targetUser,currentUser,true);
            friendRepository.delete(friend);
        } else {
            // 친구가 아닐 때
            throw new NoFriend();
        }
    }

    public User currentUser(UserDto userDto){
        return userRepository.findById(userDto.getId()).orElseThrow(()->new NoSuchElementException("User Not Found"));
    }
}
