package com.sparta.newsfeed.config;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PasswordUtil {
    final String REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!#^()%*?&])[A-Za-z\\d@$!%*?&]{8,10}$";

    /**
     * 암호가 조건에 부합하는지 확인하는 메서드
     * @param password 확인할 메서드
     * @return true : 합격 / false : 불합격
     */
    public boolean isValidPassword(String password) {
        Pattern passwordPattern = Pattern.compile(REGEX);

        return passwordPattern.matcher(password).matches();
    }
}
