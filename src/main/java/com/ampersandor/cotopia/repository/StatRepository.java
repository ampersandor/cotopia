package com.ampersandor.cotopia.repository;

import com.ampersandor.cotopia.model.Member;
import com.ampersandor.cotopia.model.Stat;

import java.time.LocalDate;
import java.util.List;

public interface StatRepository {
    Stat save(Stat stat);
    List<Stat> findByIdInRange(Member member, LocalDate from, LocalDate to);
    List<Stat> findByMember(Member member);
    List<Stat> findAll();
}
