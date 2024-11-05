package com.ampersandor.leettrack.service;

import com.ampersandor.leettrack.model.Member;
import com.ampersandor.leettrack.model.Stat;
import com.ampersandor.leettrack.model.StatResponse;
import com.ampersandor.leettrack.repository.StatRepository;

import java.time.LocalDate;
import java.util.List;

public class StatServiceImpl implements StatService {

    private final StatRepository statRepository;
    private final StatDataFetcher statDataFetcher;

    public StatServiceImpl(StatRepository statRepository, StatDataFetcher statDataFetcher) {
        this.statRepository = statRepository;
        this.statDataFetcher = statDataFetcher;
    }

    @Override
    public void updateStat(Member member) {
        StatResponse statResponse = this.statDataFetcher.fetchStat(member.getUsername());
        if (statResponse.status().equals("error")){
            throw new IllegalStateException(statResponse.message());
        }
        statRepository.save(new Stat(member.getId(), statResponse.totalSolved(), LocalDate.now()));

        // TODO: remove this
        for(int i = 0; i < 7 ; i ++){
            statRepository.save(new Stat(member.getId(), statResponse.totalSolved() - i, LocalDate.now().minusDays(i)));
        }
    }

    @Override
    public List<Stat> getStats(Member member, LocalDate startDate, LocalDate endDate) {
        return statRepository.findByIdInRange(member, startDate, endDate);
    }

    @Override
    public List<Stat> getAll(){
        return statRepository.findAll();
    }
}