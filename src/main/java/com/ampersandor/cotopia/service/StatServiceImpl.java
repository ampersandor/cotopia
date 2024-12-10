package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.entity.CodingAccount;
import com.ampersandor.cotopia.entity.Stat;
import com.ampersandor.cotopia.entity.User;
import com.ampersandor.cotopia.repository.StatRepository;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;


@Transactional
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;

    public StatServiceImpl(StatRepository statRepository) {
        this.statRepository = statRepository;
    }

    @Override
    public List<Stat> getStatsByUserBetween(Long userId, LocalDate startDate, LocalDate endDate) {
        return statRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
    }

    @Override
    public List<Stat> getStatsByTeamBetween(Long teamId, LocalDate startDate, LocalDate endDate) {
        return statRepository.findByTeamIdAndDateBetween(teamId, startDate, endDate);
    }

    @Override
    public void save(Stat stat) {
        statRepository.save(stat);
    }

    @Override
    public boolean existsByAccountIdAndDate(Long accountId, LocalDate date) {
        return statRepository.existsByAccountIdAndDate(accountId, date);
    }
}
