package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.entity.Food;
import com.ampersandor.cotopia.repository.FoodRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.util.Set;

public class FoodServiceImpl implements FoodService{
    private static final String PENDING_LIKES_KEY_PREFIX = "pending_likes:team:";

    private final RedisTemplate<String, Object> redisTemplate;
    private final FoodRepository foodRepository;

    private String getPendingLikesKey(Long teamId) {
        return PENDING_LIKES_KEY_PREFIX + teamId;
    }

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public FoodServiceImpl(RedisTemplate<String, Object> redisTemplate, FoodRepository foodRepository) {
        this.redisTemplate = redisTemplate;
        this.foodRepository = foodRepository;
    }

    @Override
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);
        
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));

        return emitter;
    }

    private void notifyClients(List<Food> foods) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        
        Map<Long, Integer> likeCounts = foods.stream()
            .collect(Collectors.toMap(Food::getId, Food::getLikes));
        
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

    @Scheduled(fixedRate = 1000)
    @Transactional
    public void flushPendingLikes() {
        Set<String> keys = redisTemplate.keys(PENDING_LIKES_KEY_PREFIX + "*");
        if (keys == null || keys.isEmpty()) {
            return;
        }

        for (String key : keys) {
            HashOperations<String, String, Integer> hashOps = redisTemplate.opsForHash();
            Map<String, Integer> allPendingLikes = hashOps.entries(key);

            if (allPendingLikes.isEmpty()) {
                continue;
            }

            List<Food> updatedFoods = new ArrayList<>();

            allPendingLikes.forEach((foodId, count) -> {
                try {
                    Long foodIdLong = Long.valueOf(foodId);
                    updatedFoods.add(foodRepository.updateLikeCount(foodIdLong, count));
                } catch (ClassCastException e) {
                    System.err.println("Invalid food ID: " + foodId);
                }
            });

            redisTemplate.delete(key);
            
            if (!updatedFoods.isEmpty()) {
                notifyClients(updatedFoods);
            }
        }
    }

    @Override
    public void addLikeCount(Long teamId, Long foodId, int likeCount) {
        HashOperations<String, String, Long> hashOps = redisTemplate.opsForHash();
        String key = getPendingLikesKey(teamId);
        hashOps.increment(key, foodId.toString(), likeCount);
    }

    @Override
    public Map<Long, Integer> getLikeCounts(Long teamId) {
        return foodRepository.findTodayFoods(teamId, LocalDate.now())
            .stream()
            .collect(Collectors.toMap(Food::getId, Food::getLikes));
    }
}