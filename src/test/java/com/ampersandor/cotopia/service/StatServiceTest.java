package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.entity.Member;
import com.ampersandor.cotopia.entity.Stat;
import com.ampersandor.cotopia.repository.MemoryStatRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

class StatServiceTest {
    private MemoryStatRepository statRepository;
    private StatService statService;

    @BeforeEach
    public void beforeEach(){
        statRepository = new MemoryStatRepository();
        statService = new StatServiceImpl(statRepository, new StatDataFetcherImpl(new RestTemplate()));
    }

    @AfterEach
    public void afterEach(){
        statRepository.clearStore();
    }

    @Test
    public void updateStatTest(){
        Member member1 = new Member("DongHunKim", "ampersandor");
        member1.setId(0L);

        statService.updateStat(member1);
        LocalDate now = LocalDate.now();
        List<Stat> stats = statService.getStats(member1, now, now.plusDays(1));
        Assertions.assertThat(stats).hasSizeGreaterThan(0);
    }

}