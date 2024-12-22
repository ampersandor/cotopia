package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.entity.Lunch;
import com.ampersandor.cotopia.entity.Team;
import com.ampersandor.cotopia.entity.Food;
import com.ampersandor.cotopia.repository.LunchRepository;
import com.ampersandor.cotopia.repository.TeamRepository;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.HashMap;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class LunchServiceImpl implements LunchService {
    
    private static final String PENDING_LIKES_KEY = "pending_likes:lunch:";
    
    private final LunchRepository lunchRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ApplicationEventPublisher eventPublisher;
    private final FoodService foodService;
    private final TeamRepository teamRepository;

    @Override
    public void addLikeCount(Long lunchId, int amount) {
        log.info("Adding like count: " + lunchId + " " + amount);
        HashOperations<String, String, Integer> hashOps = redisTemplate.opsForHash();
        String key = PENDING_LIKES_KEY + lunchId;
        hashOps.increment(key, lunchId.toString(), amount);
        log.info("Added like count: " + lunchId + " " + amount);
    }

    @Scheduled(fixedRate = 500)
    @Transactional
    public void flushPendingLikes() {
        Set<String> keys = redisTemplate.keys(PENDING_LIKES_KEY + "*");
        
        if (keys == null || keys.isEmpty()) {
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
            
            pendingLikes.forEach((lunchId, count) -> {
                try {
                    Long lunchIdLong = Long.valueOf(lunchId);
                    Lunch lunch = lunchRepository.findById(lunchIdLong)
                        .orElseThrow(() -> new EntityNotFoundException("Lunch not found with id: " + lunchIdLong));
                    
                    lunch.setLikeCount(lunch.getLikeCount() + count);
                    lunchRepository.save(lunch);
                    
                    Long teamId = lunch.getTeam().getId();
                    teamUpdates.computeIfAbsent(teamId, k -> new HashMap<>())
                        .put(lunchIdLong, lunch.getLikeCount());
                    
                } catch (NumberFormatException e) {
                    log.error("Invalid lunch ID format: {}", lunchId);
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
    public List<Lunch> getLunchesByTeamId(Long teamId, LocalDate date) {
        return lunchRepository.findByTeamIdAndCreatedAtDate(teamId, date);
    }

    @Override
    @Transactional
    public Lunch createLunch(Lunch lunch) {
        log.info("Creating new lunch for team: {}, food: {}", 
                lunch.getTeam().getId(), 
                lunch.getFood().getId());
        return lunchRepository.save(lunch);
    }

    @Override
    @Transactional
    public void createLunchesForAllTeams() {
        List<Team> teams = teamRepository.findAll();
        for (Team team : teams) {
            createLunchesForTeam(team);
        }
    }

    @Override
    @Transactional
    public void createLunchesForTeam(Team team) {
        LocalDate today = LocalDate.now();
        List<Lunch> existingLunches = getLunchesByTeamId(team.getId(), today);

        if (!existingLunches.isEmpty()) {
            log.info("Lunches already exist for team {} on date {}", team.getName(), today);
            return;
        }

        List<Food> randomFoods = foodService.getRandomFoods(5);

        for (Food food : randomFoods) {
            Lunch lunch = Lunch.builder()
                    .team(team)
                    .food(food)
                    .likeCount(0)
                    .createdAt(LocalDateTime.now())
                    .build();

            lunchRepository.save(lunch);
        }

        log.info("Created {} lunches for team: {}", randomFoods.size(), team.getName());
    }
}
