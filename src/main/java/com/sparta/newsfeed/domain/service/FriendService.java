package com.sparta.newsfeed.domain.service;

import com.sparta.newsfeed.domain.dto.FollowDto;
import com.sparta.newsfeed.domain.dto.FriendResponseDto;
import com.sparta.newsfeed.domain.dto.WaitsDto;
import com.sparta.newsfeed.domain.dto.WaitsResponseDto;
import com.sparta.newsfeed.domain.entity.Friend;
import com.sparta.newsfeed.domain.exception.AlreadyFriend;
import com.sparta.newsfeed.domain.exception.DuplicateFriendException;
import com.sparta.newsfeed.domain.exception.NoFriend;
import com.sparta.newsfeed.domain.repository.FriendRepository;
import com.sparta.newsfeed.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sparta.newsfeed.domain.entity.User;
import java.time.LocalDateTime;
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
        // user -> 로그인 중인 현재 사용자
        User user = new User();

        //friendList는 친구 신청을 하거나 받은 사람중 수락된 Friend 객체들만 전부 모은 리스트
        List<Friend> friendList = friendRepository.findByFromUserAndIsAccepted(user,true);
        friendList.addAll(friendRepository.findByToUserAndIsAccepted(user,true));

        List<FollowDto> responseList = new ArrayList<>();

        // friendList 순회 -> FollowDto 객체들을 만들어 responseList 갱신
        for (Friend friend : friendList) {
            int friendId = friend.getFromUser().getUserId() == user.getUserId()
                    ? friend.getToUser().getUserId()
                    : friend.getFromUser().getUserId();

            responseList.add(new FollowDto(friendId));
        }
        return new FriendResponseDto(responseList);
    }

    public WaitsResponseDto waitsInquiry() {

        // fromUser 추후에 JWT 에서 추출할 것
        // user -> 로그인 중인 현재 사용자
        User user = new User();

        // 수락 대기중인 Friend 객체 담은 wiatList 리스트 순회 waitsDto 담은 responseList 리스트 갱신
        List<Friend> wiatList = friendRepository.findByToUserAndIsAccepted(user,false);

        List<WaitsDto> responseList = new ArrayList<>();

        for (Friend wait : wiatList) {
            int waitsId = wait.getFromUser().getUserId();
            LocalDateTime time = LocalDateTime.parse(wait.getCreateAt());
            responseList.add(new WaitsDto(waitsId,time));
        }
        return new WaitsResponseDto(responseList);
    }

    public void reciveFriend(Integer followid) {

        // fromUser 추후에 JWT 에서 추출할 것
        // user -> 로그인 중인 현재 사용자
        User user = new User();

        //fromUser -> 친구 요청을 보낸 유저
        User fromUser = userRepository.findById(followid).orElseThrow(()->new NoSuchElementException("User Not Found"));

        // fromUser가 user에게 보낸 친구 요청 있는지 확인
        // 이미 친구 관계인지 확인
        if(friendRepository.existsByToUserAndFromUser(user, fromUser)){
            if(friendRepository.existsByToUserAndFromUserAndIsAccepted(user, fromUser, false)){
                // 수락 대기중인 친구 요청 찾았을 때 -> 수락 상태로 변화
                friendRepository.updateFriendRequestStatus(fromUser,user,true);
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

    public void deleteFriend(Integer followid) {
        // fromUser 추후에 JWT 에서 추출할 것
        // user -> 로그인 중인 현재 사용자
        User user = new User();

        User targetUser = userRepository.findById(followid).orElseThrow(()->new NoSuchElementException("User Not Found"));

        //친구 관계인지 확인
        if(friendRepository.existsByToUserAndFromUserAndIsAccepted(user, targetUser, true)) {

            //친구 일 때
            Friend friend = friendRepository.findByToUserAndFromUserAndIsAccepted(user,targetUser,true);
            friendRepository.delete(friend);
        } else if (friendRepository.existsByToUserAndFromUserAndIsAccepted(targetUser,user, true)) {
            // 친구 일 때
            Friend friend = friendRepository.findByToUserAndFromUserAndIsAccepted(targetUser,user,true);
            friendRepository.delete(friend);
        } else {
            // 친구가 아닐 때
            throw new NoFriend();
        }
    }
}
