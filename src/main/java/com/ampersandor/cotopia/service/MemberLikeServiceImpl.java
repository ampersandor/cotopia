package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.model.MemberLike;
import com.ampersandor.cotopia.repository.MemberLikeRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MemberLikeServiceImpl implements MemberLikeService{
    private static final String PENDING_LIKES_KEY = "pending_likes";

    private final RedisTemplate<String, Object> redisTemplate;
    private final MemberLikeRepository memberLikeRepository;

    // List of SseEmitters to send updates to clients
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @Override
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);
        
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));

        return emitter;
    }

    private void notifyClients() {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        
        List<MemberLike> memberLikes = memberLikeRepository.findAll();
        Map<Long, Long> likeCounts = memberLikes.stream()
            .collect(Collectors.toMap(MemberLike::getMemberId, MemberLike::getCount));
        
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("like-update")
                        .data(likeCounts));
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        });
        
        emitters.removeAll(deadEmitters);
    }

    public MemberLikeServiceImpl(RedisTemplate<String, Object> redisTemplate, MemberLikeRepository memberLikeRepository) {
        this.redisTemplate = redisTemplate;
        this.memberLikeRepository = memberLikeRepository;
    }

    @Scheduled(fixedRate = 1000) // Run every 1 seconds
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
        notifyClients();
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