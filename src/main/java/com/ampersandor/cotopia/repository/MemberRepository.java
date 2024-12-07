package com.ampersandor.cotopia.repository;

import com.ampersandor.cotopia.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    void delete(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByUsername(String username);
    List<Member> findAll();
}
