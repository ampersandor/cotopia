package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.entity.Food;
import com.ampersandor.cotopia.repository.FoodRepository;
import com.ampersandor.cotopia.event.LikeUpdateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Set;
import java.time.LocalDate;
import java.util.List;
import java.util.HashMap;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {
    
    private static final String PENDING_LIKES_KEY = "pending_likes:food:";
    
    private final FoodRepository foodRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void addLikeCount(Long foodId, int amount) {
        log.info("Adding like count: " + foodId + " " + amount);
        HashOperations<String, String, Integer> hashOps = redisTemplate.opsForHash();
        String key = PENDING_LIKES_KEY + foodId;
        hashOps.increment(key, foodId.toString(), amount);
        log.info("Added like count: " + foodId + " " + amount);
    }

    @Scheduled(fixedRate = 1000)
    @Transactional
    public void flushPendingLikes() {
        Set<String> keys = redisTemplate.keys(PENDING_LIKES_KEY + "*");
        
        if (keys == null || keys.isEmpty()) {
            log.info("No pending likes to flush");
            return;
        }
        log.info("Flushing pending likes: " + keys);
        HashOperations<String, String, Integer> hashOps = redisTemplate.opsForHash();
        
        Map<Long, Map<Long, Integer>> teamUpdates = new HashMap<>();
        
        for (String key : keys) {
            Map<String, Integer> pendingLikes = hashOps.entries(key);
            
            if (pendingLikes.isEmpty()) {
                continue;
            }
            
            pendingLikes.forEach((foodId, count) -> {
                try {
                    Long foodIdLong = Long.valueOf(foodId);
                    Food food = foodRepository.findById(foodIdLong)
                        .orElseThrow(() -> new EntityNotFoundException("Food not found with id: " + foodIdLong));
                    
                    food.setLikeCount(food.getLikeCount() + count);
                    foodRepository.save(food);
                    
                    Long teamId = food.getTeam().getId();
                    teamUpdates.computeIfAbsent(teamId, k -> new HashMap<>())
                        .put(foodIdLong, food.getLikeCount());
                    
                } catch (NumberFormatException e) {
                    log.error("Invalid food ID format: {}", foodId);
                }
            });

            redisTemplate.delete(key);
        }
        
        if (!teamUpdates.isEmpty()) {
            Map<Long, LikeUpdateEvent.LikeUpdateInfo> updates = teamUpdates.entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> new LikeUpdateEvent.LikeUpdateInfo(e.getValue())
                ));
                
            eventPublisher.publishEvent(new LikeUpdateEvent(this, updates));
        }
    }


    @Override
    @Transactional(readOnly = true)
    public List<Food> getFoodsByTeamId(Long teamId, LocalDate date) {
        return foodRepository.findByTeamIdAndCreatedAtDate(teamId, date);
    }
}
