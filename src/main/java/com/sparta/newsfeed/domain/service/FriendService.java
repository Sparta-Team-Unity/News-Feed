package com.sparta.newsfeed.domain.service;

import com.sparta.newsfeed.domain.dto.*;
import com.sparta.newsfeed.domain.entity.Friend;
import com.sparta.newsfeed.domain.exception.*;
import com.sparta.newsfeed.domain.repository.FriendRepository;
import com.sparta.newsfeed.domain.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sparta.newsfeed.domain.entity.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



@Service
@RequiredArgsConstructor
@Transactional
public class FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    //친구 신청 메서드
    public void addFriend(Integer followId, UserDto userDto) {
        User toUser = targetUser(followId);
        User currentUser = currentUser(userDto);
        if(isSamePerson(toUser, currentUser)) {
            //친구 신청 보낸 사람이랑 받은사람 같을때
            throw new UnityException(ErrorCode.SAME_PERSON);
        }
        else {
            if(isExistFriendsRequest(toUser, currentUser)) {
                // 친구 신청 보낸 사람이랑 받은사람 다르고 신청 내용 이미 존재할 때
                throw new UnityException(ErrorCode.DUPLICATE_FRIEND);
            }
            friendRepository.save(new Friend(toUser, currentUser));
        }
    }

    // 친구목록 조회 메서드
    @Transactional(readOnly = true)
    public FriendResponseDto friendsInquiry(UserDto userDto) {

        // user -> 로그인 중인 현재 사용자
        User currentUser = currentUser(userDto);
        //friendList는 친구 신청을 하거나 받은 사람중 수락된 Friend 객체들만 전부 모은 리스트
        List<Friend> friendList = findFriends(currentUser);


        List<FollowDto> responseList = new ArrayList<>();
        // friendList 순회 -> FollowDto 객체들을 만들어 responseList 갱신
        for (Friend friend : friendList) {
            int friendId = findFriendId(friend, currentUser);
            String userName = userRepository.findById(friendId).get().getName();
            responseList.add(new FollowDto(friendId, userName));
        }
        return new FriendResponseDto(responseList);
    }

    //친구 요청 수락 대기 목록 조회(로그인한 사용자에게 온 친구 요청)
    @Transactional(readOnly = true)
    public WaitsResponseDto waitsInquiry(UserDto userDto) {


        // user -> 로그인 중인 현재 사용자
        User currentUser = currentUser(userDto);
        // 수락 대기중인 Friend 객체 담은 wiatList 리스트 순회 waitsDto 담은 responseList 리스트 갱신
        List<Friend> wiatList = findFriendWaits(currentUser);
        List<WaitsDto> responseList = new ArrayList<>();

        for (Friend wait : wiatList) {
            int waitsId = wait.getFromUser().getUserId();
            LocalDateTime time = wait.getCreateAt();
            String userName = userRepository.findById(waitsId).get().getName();
            responseList.add(new WaitsDto(waitsId,time,userName));
        }
        return new WaitsResponseDto(responseList);
    }

    public void reciveFriend(Integer followId, UserDto userDto) {

        // fromUser 추후에 JWT 에서 추출할 것
        // user -> 로그인 중인 현재 사용자
        User currentUser = currentUser(userDto);
        //fromUser -> 친구 요청을 보낸 유저
        User fromUser = targetUser(followId);

        // fromUser가 user에게 보낸 친구 요청 있는지 확인
        // 이미 친구 관계인지 확인
        if(isExistFriendsRequest(fromUser, currentUser)) {
            if(IsWaitRequest(currentUser, fromUser)){
                // 수락 대기중인 친구 요청 찾았을 때 -> 수락 상태로 변화
                reciveFriendRequest(fromUser, currentUser);
            }
            else {
                //이미 친구가 된 요청 찾았을 때
                throw new UnityException(ErrorCode.ALREADY_FRIEND);
            }
        }else {
            // 친구 요청 찾을 수 없을 때
            throw new UnityException(ErrorCode.CANNOTFOUND_FRIENDREQUEST);
        }
    }


    public void deleteFriend(Integer followId, UserDto userDto) {
        // fromUser 추후에 JWT 에서 추출할 것
        // user -> 로그인 중인 현재 사용자
        User currentUser = currentUser(userDto);
        User targetUser = targetUser(followId);
        //자기 자신인지 확인
        if(isSamePerson(currentUser, targetUser)) {
            throw new UnityException(ErrorCode.SAME_PERSON);
        }
        else {
            //친구 관계인지 확인
            //메서드 따로 빼기

            if(IsAlreadyFriend(currentUser, targetUser)) {
                //친구 일 때
                Friend friend = findFriend(currentUser, targetUser);
                friendRepository.delete(friend);
            } else if (IsAlreadyFriend(targetUser, currentUser)) {
                // 친구 일 때
                Friend friend = findFriend(targetUser, currentUser);
                friendRepository.delete(friend);
            } else {
                // 친구가 아닐 때
                throw new UnityException(ErrorCode.NOT_FRIEND);
            }
        }

    }
    //------------------------------------ 유틸 메서드 ----------------------------------------------//

    public User currentUser(UserDto userDto){
        return userRepository.findById(userDto.getId()).orElseThrow(()->new UnityException(ErrorCode.USER_NOT_EXIST));
    }

    public User targetUser(Integer followid) {
        return userRepository.findById(followid).orElseThrow(()->new UnityException(ErrorCode.USER_NOT_EXIST));
    }
    public boolean isSamePerson(User user1, User user2){
        if(user1.equals(user2)){
            return true;
        }
        return false;
    }
    public boolean isExistFriendsRequest(User user1, User user2){
        if(friendRepository.existsByToUserAndFromUser(user1, user2)
                || friendRepository.existsByToUserAndFromUser(user2, user1)){
            return true;
        }
        return false;
    }
    public List<Friend> findFriends(User user) {
        List<Friend> friendList = friendRepository.findByFromUserAndIsAccepted(user,true);
        friendList.addAll(friendRepository.findByToUserAndIsAccepted(user,true));
        return friendList;
    }

    public int findFriendId(Friend friend, User user) {
        if(friend.getFromUser().getUserId() == user.getUserId()){
            return friend.getToUser().getUserId();
        }
        else {
            return friend.getFromUser().getUserId();
        }
    }
    public List<Friend> findFriendWaits(User user) {
       return friendRepository.findByToUserAndIsAccepted(user,false);
    }
    public boolean IsWaitRequest(User user1, User user2) {
        if(friendRepository.existsByToUserAndFromUserAndIsAccepted(user1, user2, false)){
            return true;
        }
        return false;
    }
    public void reciveFriendRequest(User user1, User user2) {
        friendRepository.updateFriendRequestStatus(user1,user2,true);
    }
    public boolean IsAlreadyFriend(User user1, User user2) {
        if(friendRepository.existsByToUserAndFromUserAndIsAccepted(user1, user2, true)){
            return true;
        }
        return false;
    }
    public Friend findFriend(User user1, User user2) {
        Friend friend = friendRepository.findByToUserAndFromUserAndIsAccepted(user1,user2,true);
        return friend;
    }
}
