package com.sparta.newsfeed.domain.exception;

public class PasswordEqualsException extends RuntimeException{
    public PasswordEqualsException() {
        super("바꾸려는 비밀번호와 기존의 비밀번호가 동일합니다.");
    }
}
