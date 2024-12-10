package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.entity.Stat;
import java.time.LocalDate;
import java.util.List;

public interface StatService {
    List<Stat> getStatsByUserBetween(Long userId, LocalDate startDate, LocalDate endDate);
    List<Stat> getStatsByTeamBetween(Long teamId, LocalDate startDate, LocalDate endDate);
    void save(Stat stat);
    boolean existsByAccountIdAndDate(Long accountId, LocalDate date);
}
