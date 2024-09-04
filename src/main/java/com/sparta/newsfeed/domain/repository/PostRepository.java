package com.sparta.newsfeed.domain.repository;

import com.sparta.newsfeed.domain.entity.Post;
import com.sparta.newsfeed.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    Page<Post> findByUserInOrderByCreateAtDesc(List<User> userList, Pageable pageable);

}
