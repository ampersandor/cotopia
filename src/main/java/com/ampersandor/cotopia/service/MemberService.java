package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    Long join(Member member);
    List<Member> findMembers();
    Optional<Member> findOne(Long memberId);
}

