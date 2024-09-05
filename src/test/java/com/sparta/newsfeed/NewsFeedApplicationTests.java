package com.sparta.newsfeed;

import com.sparta.newsfeed.config.passwordconfig.PasswordUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NewsFeedApplicationTests {

    @Test
    void contextLoads() {
        PasswordUtil passwordUtil = new PasswordUtil();

        System.out.println(passwordUtil.isValidPassword("rlarjsdn1@"));
    }

}
