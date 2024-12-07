package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.entity.Member;
import com.ampersandor.cotopia.entity.Stat;

import java.time.LocalDate;
import java.util.List;

public interface StatService {
    void updateStat(Member member);
    List<Stat> getStats(Member member);
    List<Stat> getStats(Member member, LocalDate startDate, LocalDate endDate);
    List<Stat> getAll();

}
