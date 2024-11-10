package com.ampersandor.leettrack.service;

import com.ampersandor.leettrack.model.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    Long join(Member member);
    List<Member> findMembers();
    Optional<Member> findOne(Long memberId);
}

