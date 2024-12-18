package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.entity.Food;
import com.ampersandor.cotopia.repository.FoodRepository;
import com.ampersandor.cotopia.event.LikeUpdateEvent;
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

import jakarta.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {
    
    private static final String PENDING_LIKES_KEY = "pending_likes:food:";
    
    private final FoodRepository foodRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void addLikeCount(Long foodId, int amount) {
        HashOperations<String, String, Integer> hashOps = redisTemplate.opsForHash();
        String key = PENDING_LIKES_KEY + foodId;
        hashOps.increment(key, foodId.toString(), amount);
    }

    @Scheduled(fixedRate = 500)  // 0.5초마다 실행
    @Transactional
    public void flushPendingLikes() {
        Set<String> keys = redisTemplate.keys(PENDING_LIKES_KEY + "*");
        if (keys == null || keys.isEmpty()) {
            return;
        }
        HashOperations<String, String, Integer> hashOps = redisTemplate.opsForHash();
        
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
                    
                    eventPublisher.publishEvent(new LikeUpdateEvent(this, foodIdLong, food.getLikeCount()));
                    
                } catch (NumberFormatException e) {
                    System.err.println("Invalid food ID format: " + foodId);
                }
            });

            redisTemplate.delete(key);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public List<Food> getFoodsByTeamId(Long teamId, LocalDate date) {
        return foodRepository.findByTeamIdAndCreatedAtDate(teamId, date);
    }
}
