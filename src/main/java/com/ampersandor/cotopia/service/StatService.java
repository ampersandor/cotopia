package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.entity.User;
import com.ampersandor.cotopia.entity.Stat;
import com.ampersandor.cotopia.entity.CodingAccount;
import java.time.LocalDate;
import java.util.List;

public interface StatService {
    List<Stat> getStatsByUserBetween(User user, LocalDate startDate, LocalDate endDate);
    List<Stat> getStatsByTeamBetween(Long teamId, LocalDate startDate, LocalDate endDate);
    List<Stat> getStatsByCodingAccountOnDate(CodingAccount codingAccount, LocalDate date);
    void save(Stat stat);
    boolean existsByAccountIdAndDate(Long accountId, LocalDate date);
}
