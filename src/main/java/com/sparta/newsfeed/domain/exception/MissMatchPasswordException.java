package com.sparta.newsfeed.domain.exception;

public class MissMatchPasswordException extends RuntimeException{
    public MissMatchPasswordException() {
        super("현재 입력하신 비밀번호가 기존 비밀번호와 일치하지 않습니다.");
    }
}
