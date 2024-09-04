package com.sparta.newsfeed.domain.exception;

public class DuplicateFriendException extends RuntimeException {
    public DuplicateFriendException() {
        super("Duplicate friend");
    }
}
