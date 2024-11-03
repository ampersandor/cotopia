package com.ampersandor.leettrack.repository;

import com.ampersandor.leettrack.model.Member;
import com.ampersandor.leettrack.model.Stat;

import java.time.LocalDate;
import java.util.List;

public interface StatRepository {
    Stat save(Stat stat);
    List<Stat> findByIdInRange(Member member, LocalDate from, LocalDate to);
    List<Stat> findAll();
}
