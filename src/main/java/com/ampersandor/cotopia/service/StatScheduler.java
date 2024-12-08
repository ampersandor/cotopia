package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.entity.CodingAccount;
import com.ampersandor.cotopia.entity.Stat;
import com.ampersandor.cotopia.repository.CodingAccountRepository;
import com.ampersandor.cotopia.repository.StatRepository;
import com.ampersandor.cotopia.util.CodingPlatformFetcher;
import com.ampersandor.cotopia.util.StatResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StatScheduler {

    private final CodingAccountRepository codingAccountRepository;
    private final StatService statService;
    private final Map<String, CodingPlatformFetcher> fetchers;

    @Scheduled(fixedRate = 600000) // Run every 1 minute
    public void updateStat() {
        System.out.println("Update Stat!!");
        System.out.println("fetchers = " + fetchers);
        List<CodingAccount> codingAccounts = codingAccountRepository.findAll();
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
                    .user(codingAccount.getUser())
                    .date(today)
                    .problemsSolved(statResponse.totalSolved())
                    .platform(codingAccount.getPlatform())
                    .build();
            statService.save(stat);
        }
    }
}
