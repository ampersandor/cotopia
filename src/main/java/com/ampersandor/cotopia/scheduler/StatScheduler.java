package com.ampersandor.cotopia.scheduler;

import com.ampersandor.cotopia.entity.CodingAccount;
import com.ampersandor.cotopia.entity.Stat;
import com.ampersandor.cotopia.repository.CodingAccountRepository;
import com.ampersandor.cotopia.service.StatService;
import com.ampersandor.cotopia.util.CodingPlatformFetcher;
import com.ampersandor.cotopia.util.StatResponse;

import jakarta.annotation.PostConstruct;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
@Slf4j
public class StatScheduler {

    private final CodingAccountRepository codingAccountRepository;
    private final StatService statService;
    private final Map<String, CodingPlatformFetcher> fetchers;
    
    @PostConstruct
    public void init() {
        log.info("Initialized StatScheduler with fetchers: {}", fetchers.keySet());
    }

    @Scheduled(fixedRate = 600000) // Run every 1 minute
    public void updateStat() {
        List<CodingAccount> codingAccounts = codingAccountRepository.findAll();
        if (codingAccounts == null || codingAccounts.isEmpty()) {
            log.info("No coding accounts found");
            return;
        }
        log.info("start update stat");
        for (CodingAccount codingAccount : codingAccounts) {
            LocalDate today = LocalDate.now();
            if (statService.existsByAccountIdAndDate(codingAccount.getId(), today)) continue;
            CodingPlatformFetcher fetcher = fetchers.get(codingAccount.getPlatform());
            if (fetcher == null) {
                throw new IllegalStateException("Fetcher for platform " + codingAccount.getPlatform() + " not found");
            }

            StatResponse statResponse = fetcher.fetchStat(codingAccount.getPlatformId());
            if (statResponse.status().equals("error")) {
                throw new IllegalStateException(statResponse.message());
            }
   
            Stat stat = Stat.builder()
                    .codingAccount(codingAccount)
                    .date(today)
                    .problemsSolved(statResponse.totalSolved())
                    .platform(codingAccount.getPlatform())
                    .build();
            statService.save(stat);
        }
        log.info("end update stat");
    }
}
