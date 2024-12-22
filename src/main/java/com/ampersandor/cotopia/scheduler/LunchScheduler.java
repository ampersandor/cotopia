package com.ampersandor.cotopia.scheduler;

import com.ampersandor.cotopia.service.LunchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LunchScheduler {
    
    private final LunchService lunchService;
    
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void createDailyLunches() {
        log.info("Starting lunch creation check...");
        lunchService.createLunchesForAllTeams();
        log.info("Completed lunch creation check");
    }
}
