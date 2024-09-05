package com.sparta.newsfeed.domain.repository;

import com.sparta.newsfeed.domain.entity.Post;
import com.sparta.newsfeed.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {

    /**
     * 전달받은 사용자를 생성일 기준 내림차순으로 조회
     * @param userList
     * @param pageable
     * @return
     */
    Optional<Page<Post>> findByUserInOrderByCreateAtDesc(List<User> userList, Pageable pageable);

}
