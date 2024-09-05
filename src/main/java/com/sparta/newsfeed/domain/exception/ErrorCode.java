package com.sparta.newsfeed.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // Token ErrordeCode
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "재로그인 해주세요."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "로그아웃 상태입니다."),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "토큰이 존재하지 않습니다."),
    JWT_NOT_SUPPORT(HttpStatus.HTTP_VERSION_NOT_SUPPORTED, "토큰을 지원하지 않습니다"),
    // User ErrorCode
    USER_NOT_EXIST(HttpStatus.NOT_FOUND, "User Not Found"),
    MISS_MATCH_PASSWORD(HttpStatus.UNAUTHORIZED, "현재 입력하신 비밀번호가 기존 비밀번호와 일치하지 않습니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 틀립니다."),
    INVALID_PASSWORD_FORM(HttpStatus.NOT_ACCEPTABLE, "비밀번호 형식이 올바르지 않습니다."),
    CONFLICT_EMAIL(HttpStatus.CONFLICT, "이미 등록된 이메일 입니다."),

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
    AUTHOR_NOT_MATCH(HttpStatus.UNAUTHORIZED, "작성자가 일치하지 않습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND,"작성된 게시물이 없습니다.");


    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message){
        this.status = httpStatus;
        this.message = message;
    }

}
