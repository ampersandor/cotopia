package com.ampersandor.cotopia.repository;

import com.ampersandor.cotopia.entity.Member;
import com.ampersandor.cotopia.entity.Stat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryStatRepository implements StatRepository{
    private final static Map<Long, Stat> store = new HashMap<>();
    private static long sequence = 0L;


    @Override
    public Stat save(Stat stat) {
        stat.setId(++sequence);
        store.put(stat.getId(), stat);
        return stat;
    }

    @Override
    public List<Stat> findByIdInRange(Member member, LocalDate from, LocalDate to) {
        return store.values().stream()
                .filter(stat -> stat.getUserId().equals(member.getId())
                        && stat.getDate().isBefore(to)
                        && stat.getDate().isAfter(from.minusDays(1)))
                .toList();
    }

    @Override
    public List<Stat> findByMember(Member member) {
        return store.values().stream()
                .filter(stat -> stat.getUserId().equals(member.getId()))
                .toList();
    }

    @Override
    public List<Stat> findAll(){
        return new ArrayList<>(store.values());
    }

    public void clearStore(){
        store.clear();
    }

}
