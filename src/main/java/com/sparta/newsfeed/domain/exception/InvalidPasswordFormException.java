package com.sparta.newsfeed.domain.exception;

public class InvalidPasswordFormException extends RuntimeException{
    public InvalidPasswordFormException() {
        super("비밀번호 형식이 올바르지 않습니다.");
    }
}
