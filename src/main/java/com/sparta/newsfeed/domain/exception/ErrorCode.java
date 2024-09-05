package com.sparta.newsfeed.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // User ErrorCode
    USER_NOT_EXIST(HttpStatus.NOT_FOUND, "User Not Found"),

    // Profile ErrorCode

    // Friend ErrorCode
    ALREADY_FRIEND(HttpStatus.CONFLICT, "이미 친구입니다."),
    DUPLICATE_FRIEND(HttpStatus.CONFLICT, "Duplicate Friend"),
    NOT_FRIEND(HttpStatus.UNAUTHORIZED, "해당 유저와 친구가 아닙니다."),


    // Post ErrorCode
    POST_NOT_EXIST(HttpStatus.NOT_FOUND, "Post Not Found"),
    AUTHOR_NOT_MATCH(HttpStatus.UNAUTHORIZED, "작성자가 일치하지 않습니다.");


    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message){
        this.status = httpStatus;
        this.message = message;
    }

}
