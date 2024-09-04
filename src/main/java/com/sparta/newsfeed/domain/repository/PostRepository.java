package com.sparta.newsfeed.domain.repository;

import com.sparta.newsfeed.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
