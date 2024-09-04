package com.sparta.newsfeed.domain.repository;

import com.sparta.newsfeed.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Optional<Post> findById(Integer postId);
}
