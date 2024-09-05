package com.sparta.newsfeed.domain.exception;

public class NoFriend extends RuntimeException{
    public NoFriend() {
        super("해당 유저와 친구가 아닙니다.");
    }
}
