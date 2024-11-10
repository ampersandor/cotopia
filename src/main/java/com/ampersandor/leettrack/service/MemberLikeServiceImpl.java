package com.ampersandor.leettrack.service;

import com.ampersandor.leettrack.model.MemberLike;
import com.ampersandor.leettrack.repository.MemberLikeRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberLikeServiceImpl implements MemberLikeService{
    private static final String PENDING_LIKES_KEY = "pending_likes";

    private final RedisTemplate<String, Object> redisTemplate;
    private final MemberLikeRepository memberLikeRepository;

    public MemberLikeServiceImpl(RedisTemplate<String, Object> redisTemplate, MemberLikeRepository memberLikeRepository) {
        this.redisTemplate = redisTemplate;
        this.memberLikeRepository = memberLikeRepository;
    }

    @Scheduled(fixedRate = 5000) // Run every 5 seconds
    @Transactional
    public void flushPendingLikes() {
        HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();

        // Get all pending likes
        Map<String, Object> allPendingLikes = hashOps.entries(PENDING_LIKES_KEY);
        if (allPendingLikes.isEmpty()) {
            return;
        }

        // Process likes
        allPendingLikes.forEach((memberId, count) -> {
            try {
                // Ensure memberId is parsed as Long only once
                Long memberIdLong = Long.valueOf(memberId);
                // Find existing like count or create a new record
                MemberLike like = memberLikeRepository
                        .findByMemberId(memberIdLong)
                        .orElse(new MemberLike(memberIdLong, 0L));

                Long convertedValue = count instanceof Integer ? ((Integer) count).longValue() : (Long) count;

                // Increment like count
                like.setCount(like.getCount() + convertedValue);

                // Save the updated like count
                memberLikeRepository.save(like);
            } catch (ClassCastException e) {
                // Handle the case where memberId is not a valid Long
                System.err.println("Invalid member ID: " + memberId);
            }
        });
        // Clear processed likes
        redisTemplate.delete(PENDING_LIKES_KEY);
    }

    @Override
    public void addLikeCount(Long memberId) {
        HashOperations<String, String, Long> hashOps = redisTemplate.opsForHash();

        // Increment the count for this member
        hashOps.increment(PENDING_LIKES_KEY, memberId.toString(), 1L);
    }

    @Override
    public Long getLikeCount(Long memberId) {
        return this.memberLikeRepository.getLikeCount(memberId);
    }

    @Override
    public Map<Long, Long> getLikeCounts(List<Long> memberIds) {
        Map<Long, Long> likeCounts = new HashMap<>();
        for (Long memberId : memberIds) {
            likeCounts.put(memberId, getLikeCount(memberId));
        }
        return likeCounts;
    }
}