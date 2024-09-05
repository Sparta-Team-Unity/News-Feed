package com.sparta.newsfeed.domain.exception;

public class SamePersonException extends RuntimeException {
    public SamePersonException() {
        super("자신에게는 친구 신청할 수 없습니다.");
    }
}
