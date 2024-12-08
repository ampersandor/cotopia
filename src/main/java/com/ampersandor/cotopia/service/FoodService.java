package com.ampersandor.cotopia.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface FoodService {
    void addLikeCount(Long teamId, Long foodId, int likeCount);
    Map<Long, Integer> getLikeCounts(Long teamId);
    SseEmitter subscribe();
}