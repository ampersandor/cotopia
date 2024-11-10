package com.ampersandor.leettrack.repository;

import com.ampersandor.leettrack.model.MemberLike;

import java.util.Optional;

public interface MemberLikeRepository {
    Optional<MemberLike> findByMemberId(Long memberId);
    Long getLikeCount(Long memberId);
    void save(MemberLike memberLike);

}
