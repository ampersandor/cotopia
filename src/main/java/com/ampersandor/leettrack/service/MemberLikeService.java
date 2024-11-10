package com.ampersandor.leettrack.service;

import java.util.List;
import java.util.Map;

public interface MemberLikeService {
    void addLikeCount(Long memberId);
    Long getLikeCount(Long memberId);
    Map<Long, Long> getLikeCounts(List<Long> memberIds);
}