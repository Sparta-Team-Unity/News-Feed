package com.sparta.newsfeed.domain.exception;

public class PostNotExistException extends RuntimeException {
    public PostNotExistException() {super("Post Not Exist");}
}
