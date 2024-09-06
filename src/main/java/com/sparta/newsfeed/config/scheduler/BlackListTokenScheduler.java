package com.sparta.newsfeed.config.scheduler;

import com.sparta.newsfeed.domain.service.BlacklistTokenService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BlackListTokenScheduler {
    private final BlacklistTokenService blacklistTokenService;

    public BlackListTokenScheduler(BlacklistTokenService blacklistTokenService) {
        this.blacklistTokenService = blacklistTokenService;
    }

    /**
     * 1시간마다 BlackListToken 테이블을 순회하며 만료된 토큰을 파괴한다.
     */
    @Scheduled(cron = "0 0 0/1 * * *")
    public void blackListTokens() {
        System.out.println("돌아가고 있다");
        blacklistTokenService.clearExpirestoken();
    }
}
