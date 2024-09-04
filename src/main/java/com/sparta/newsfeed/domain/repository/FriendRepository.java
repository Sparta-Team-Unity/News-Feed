package com.sparta.newsfeed.domain.repository;

import com.sparta.newsfeed.domain.entity.Friend;
import com.sparta.newsfeed.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;



public interface FriendRepository extends JpaRepository<Friend, Integer> {
    boolean existsByToUserAndFromUser(User toUser, User fromUser);
    List<Friend> findByFromUserAndIsAccepted(User fromUser, boolean isAccepted);
    List<Friend> findByToUserAndIsAccepted(User toUser,boolean isAccepted);


}
