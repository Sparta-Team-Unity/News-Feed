package com.sparta.newsfeed.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 비밀번호 오류 Exception
     * @param ex 오류 상태, 메시지
     * @return 해당 내용이 담긴 에러 객체
     */
    @ExceptionHandler(UnityException.class)
    public ResponseEntity<Map<String, Object>> handlePasswordMismatchException(UnityException ex) {
        return getErrorResponse(ex.getErrorCode().getStatus(), ex.getMessage());
    }

    /**
     * 에러 객체를 반환하는 메서드
     * @param status 오류 상태
     * @param message 오류 메시지
     * @return 해당 내용이 담긴 에러 객체
     */
    public ResponseEntity<Map<String, Object>> getErrorResponse(HttpStatus status, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", status.name());
        errorResponse.put("code", status.value());
        errorResponse.put("message", message);

        return new ResponseEntity<>(errorResponse, status);
    }
}