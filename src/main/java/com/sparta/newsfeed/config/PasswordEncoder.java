package com.sparta.newsfeed.config;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {
    public String encode(String rawPassword) {
        return BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, rawPassword.toCharArray());
    }

    /**
     * 비밀번호가 일치하는지 확인하는 메서드
     * @param rawPassword 평문( encoding 되기 전의 원문 )
     * @param encodedPassword 암호화된 평문
     * @return True : 일치 / False : 불일치
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword);
        return result.verified;
    }
}

