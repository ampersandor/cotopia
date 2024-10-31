package com.ampersandor.leettrack.repository;

import com.ampersandor.leettrack.model.Member;

import java.util.*;

public class MemoryMemberRepository implements MemberRepository{

    private final static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;


    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Member> findByUsername(String lcId) {
        return store.values().stream()
                .filter(member -> member.getUsername().equals(lcId))
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore(){
        store.clear();
    }
}
