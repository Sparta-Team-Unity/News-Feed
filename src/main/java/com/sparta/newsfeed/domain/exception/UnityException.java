package com.sparta.newsfeed.domain.exception;

import lombok.Getter;

@Getter
public class UnityException extends RuntimeException{
    private ErrorCode errorCode;

    public UnityException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
