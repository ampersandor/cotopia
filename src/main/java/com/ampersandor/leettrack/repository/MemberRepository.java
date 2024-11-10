package com.ampersandor.leettrack.repository;

import com.ampersandor.leettrack.model.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    void delete(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByUsername(String username);
    List<Member> findAll();
}
