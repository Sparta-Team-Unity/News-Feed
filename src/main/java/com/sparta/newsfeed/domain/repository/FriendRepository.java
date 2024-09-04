package com.sparta.newsfeed.domain.repository;

import com.sparta.newsfeed.domain.entity.Friend;
import com.sparta.newsfeed.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface FriendRepository extends JpaRepository<Friend, Integer> {
    boolean existsByToUserAndFromUser(User toUser, User fromUser);
}
