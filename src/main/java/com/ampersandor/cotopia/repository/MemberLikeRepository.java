package com.ampersandor.cotopia.repository;

import com.ampersandor.cotopia.model.MemberLike;

import java.util.List;
import java.util.Optional;

public interface MemberLikeRepository {
    Optional<MemberLike> findByMemberId(Long memberId);
    List<MemberLike> findAll();
    Long getLikeCount(Long memberId);
    void save(MemberLike memberLike);

}
