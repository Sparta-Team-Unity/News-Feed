package com.sparta.newsfeed.domain.exception;

public class UserNotExistException extends RuntimeException {
    public UserNotExistException() {super("User Not Exist");}
}
