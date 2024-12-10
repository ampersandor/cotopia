package com.ampersandor.cotopia.repository;

import com.ampersandor.cotopia.entity.Stat;

import java.time.LocalDate;
import java.util.List;

public interface StatRepository {
    Stat save(Stat stat);
    List<Stat> findByAccountIdAndDateBetween(Long accountId, LocalDate from, LocalDate to);
    List<Stat> findByUserIdAndDateBetween(Long userId, LocalDate from, LocalDate to);
    List<Stat> findByTeamIdAndDateBetween(Long teamId, LocalDate from, LocalDate to);
    boolean existsByAccountIdAndDate(Long accountId, LocalDate date);
}
