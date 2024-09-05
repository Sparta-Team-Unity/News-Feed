package com.sparta.newsfeed.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // User ErrorCode
    USER_NOT_EXIST(HttpStatus.NOT_FOUND, "User Not Found"),
    MISS_MATCH_PASSWORD(HttpStatus.UNAUTHORIZED, "현재 입력하신 비밀번호가 기존 비밀번호와 일치하지 않습니다."),
    INVALID_PASSWORD_FORM(HttpStatus.NOT_ACCEPTABLE, "비밀번호 형식이 올바르지 않습니다."),

    // Profile ErrorCode
    DUPLICATE_PASSWORD(HttpStatus.NOT_ACCEPTABLE, "바꾸려는 비밀번호와 기존의 비밀번호가 동일합니다."),

    // Friend ErrorCode
    SAME_PERSON(HttpStatus.CONFLICT, "자기 자신에게 친구 신청할 수 없습니다."),
    ALREADY_FRIEND(HttpStatus.CONFLICT, "이미 친구입니다."),
    DUPLICATE_FRIEND(HttpStatus.CONFLICT, "이미 친구 신청을 하셨거나 친구입니다."),
    NOT_FRIEND(HttpStatus.UNAUTHORIZED, "해당 유저와 친구가 아닙니다."),
    CANNOTFOUND_FRIENDREQUEST(HttpStatus.CONFLICT, "해당 친구 요청을 찾을 수 없습니다."),


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
