package com.sparta.newsfeed.domain.repository;

import com.sparta.newsfeed.domain.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FriendRepository extends JpaRepository<Friend, Long> {
}
