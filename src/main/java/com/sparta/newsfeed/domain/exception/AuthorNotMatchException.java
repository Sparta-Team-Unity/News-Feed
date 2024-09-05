package com.sparta.newsfeed.domain.exception;

public class AuthorNotMatchException extends RuntimeException {
    public AuthorNotMatchException() {super("작성자가 일치하지 않습니다.");}
}
