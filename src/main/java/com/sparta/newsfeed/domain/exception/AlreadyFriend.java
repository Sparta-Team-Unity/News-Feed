package com.sparta.newsfeed.domain.exception;

public class AlreadyFriend extends RuntimeException {
    public AlreadyFriend() {
        super("이미 친구입니다");
    }
}
