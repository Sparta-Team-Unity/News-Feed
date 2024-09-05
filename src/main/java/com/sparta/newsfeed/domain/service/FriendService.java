package com.sparta.newsfeed.domain.service;

import com.sparta.newsfeed.domain.dto.friend.FollowDto;
import com.sparta.newsfeed.domain.dto.friend.FriendResponseDto;
import com.sparta.newsfeed.domain.dto.friend.WaitsDto;
import com.sparta.newsfeed.domain.dto.friend.WaitsResponseDto;
import com.sparta.newsfeed.domain.dto.user.UserDto;
import com.sparta.newsfeed.domain.entity.Friend;
import com.sparta.newsfeed.domain.exception.*;
import com.sparta.newsfeed.domain.repository.FriendRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sparta.newsfeed.domain.entity.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Transactional
public class FriendService {
    private final FriendRepository friendRepository;
    private final UserService userService;

    /**
     * 친구 추가하는 메서드
     * @param followId 추가할 친구 Id
     * @param userDto 현재 로그인 중인 유저 정보
     */
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

    /**
     * 현재 로그인 중인 유저의 친구 목록을 조회하는 메서드
     * @param userDto 로그인 중인 유저 정보
     * @return 해당 유저의 친구 목록
     */
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
            String userName = userService.findUserById(friendId).getName();
            responseList.add(new FollowDto(friendId, userName));
        }
        return new FriendResponseDto(responseList);
    }

    /**
     * 친구 요청 수락 대기 목록 조회(로그인한 사용자에게 온 친구 요청)
     * @param userDto 로그인 한 유저 정보
     * @return 친구 요청 수락 대기 목록
     */
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
            String userName = userService.findUserById(waitsId).getName();
            responseList.add(new WaitsDto(waitsId,time,userName));
        }
        return new WaitsResponseDto(responseList);
    }

    /**
     * 친구 요청을 수락하는 메서드
     * @param followId 상대방 Id
     * @param userDto 현재 로그인 중인 유저 정보
     */
    public void reciveFriend(Integer followId, UserDto userDto) {

        // fromUser 추후에 JWT 에서 추출할 것
        // user -> 로그인 중인 현재 사용자
        User currentUser = currentUser(userDto);
        //fromUser -> 친구 요청을 보낸 유저
        User fromUser = targetUser(followId);

        // fromUser가 user에게 보낸 친구 요청 있는지 확인
        // 이미 친구 관계인지 확인
        if(isExistFriendsRequest(fromUser, currentUser)) {
            if(isWaitRequest(currentUser, fromUser)){
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

    /**
     * 친구 삭제하는 메서드
     * @param followId 상대방 Id
     * @param userDto 현재 로그인 중인 유저 정보
     */
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

    /**
     * 현재 로그인된 유저 정보를 반환하는 메서드
     * @param userDto 현재 로그인된 유저 정보
     * @return 로그인된 유저 Entity
     */
    public User currentUser(UserDto userDto){
        return userService.findUserById(userDto.getId());
    }

    /**
     * 상대방 Id로 유저를 조회하는 메서드
     * @param followid 상대방 Id
     * @return 조회된 상대방 Id
     */
    public User targetUser(Integer followid) {
        return userService.findUserById(followid);
    }

    /**
     * 두 유저가 일치하는지 확인하는 메서드
     * @param user1 유저 A
     * @param user2 유저 B
     * @return True : A, B가 동일인물 / False : A, B가 다른인물
     */
    public boolean isSamePerson(User user1, User user2){
        return user1.equals(user2);
    }

    /**
     * 이미 친구 요청이 되어있는 상태인지 확인하는 메서드
     * @param user1 유저 A
     * @param user2 유저 B
     * @return True : 친구 요청이 이미 존재하는 상태 / False : 친구 요청이 존재하지 않는 상태
     */
    @Transactional(readOnly = true)
    public boolean isExistFriendsRequest(User user1, User user2){
        return friendRepository.existsByToUserAndFromUser(user1, user2)
                || friendRepository.existsByToUserAndFromUser(user2, user1);
    }

    /**
     * 해당 User의 친구 목록을 조회하는 메서드
     * @param user 유저 정보
     * @return 해당 유저의 친구 목록
     */
    @Transactional(readOnly = true)
    public List<Friend> findFriends(User user) {
        List<Friend> friendList = friendRepository.findByFromUserAndIsAccepted(user,true);
        friendList.addAll(friendRepository.findByToUserAndIsAccepted(user,true));
        return friendList;
    }

    /**
     * 해당 유저랑 친구 상태인 상대 유저의 Id를 조회하는 메서드
     * @param friend 친구 Id
     * @param user User Id
     * @return 상대 유저 Id
     */
    public int findFriendId(Friend friend, User user) {
        if(Objects.equals(friend.getFromUser().getUserId(), user.getUserId())){
            return friend.getToUser().getUserId();
        }
        else {
            return friend.getFromUser().getUserId();
        }
    }

    /**
     * 해당 유저에게 존재하는 친구 요청 목록을 반환하는 메서드
     * @param user 유저
     * @return 친구 요청 목록
     */
    @Transactional(readOnly = true)
    public List<Friend> findFriendWaits(User user) {
       return friendRepository.findByToUserAndIsAccepted(user,false);
    }

    /**
     * 두 유저가 현재 친구 요청 상태인지 반환하는 메서드
     * @param user1 유저A
     * @param user2 유저B
     * @return True : A, B 둘 중 한명이 친구요청 보낸 상태 / False : 이미 친구 상태이거나, 친구가 아닌 경우
     */
    @Transactional(readOnly = true)
    public boolean isWaitRequest(User user1, User user2) {
        return friendRepository.existsByToUserAndFromUserAndIsAccepted(user1, user2, false);
    }

    /**
     * 두 유저의 친구 요청 수락 상태로 업데이트 하는 메서드
     * @param user1 유저A
     * @param user2 유저B
     */
    @Transactional
    public void reciveFriendRequest(User user1, User user2) {
        friendRepository.updateFriendRequestStatus(user1,user2,true);
    }

    /**
     * 두 유저가 이미 친구 상태인지 확인하는 메서드
     * @param user1 유저A
     * @param user2 유저B
     * @return True : 이미 친구 상태 / False : 친구가 아닌 상태
     */
    @Transactional(readOnly = true)
    public boolean IsAlreadyFriend(User user1, User user2) {
        return friendRepository.existsByToUserAndFromUserAndIsAccepted(user1, user2, true);
    }

    /**
     * 두 유저가 서로 친구 상태인 경우인 Friend를 반환하는 메서드
     * @param user1 유저A
     * @param user2 유저B
     * @return 친구 객체
     */
    @Transactional(readOnly = true)
    public Friend findFriend(User user1, User user2) {
        return friendRepository.findByToUserAndFromUserAndIsAccepted(user1,user2,true);
    }
}
