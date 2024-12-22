package com.ampersandor.cotopia.scheduler;

import com.ampersandor.cotopia.entity.Food;
import com.ampersandor.cotopia.entity.Lunch;
import com.ampersandor.cotopia.entity.Team;
import com.ampersandor.cotopia.repository.TeamRepository;
import com.ampersandor.cotopia.service.FoodService;
import com.ampersandor.cotopia.service.LunchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LunchScheduler {
    
    private final TeamRepository teamRepository;
    private final FoodService foodService;
    private final LunchService lunchService;
    
    @Transactional
    @Scheduled(cron = "0 0 9 * * *", zone = "Asia/Seoul")
    public void createDailyLunches() {
        log.info("Starting lunch creation check...");
        
        try {
            List<Team> teams = teamRepository.findAll();
            LocalDate today = LocalDate.now();
            
            for (Team team : teams) {
                try {
                    List<Lunch> existingLunches = lunchService.getLunchesByTeamId(team.getId(), today);
                    
                    if (!existingLunches.isEmpty()) {
                        log.info("Lunches already exist for team {} on date {}", team.getName(), today);
                        continue;
                    }
                    
                    List<Food> randomFoods = foodService.getRandomFoods(5);
                    
                    // 각 음식에 대해 Lunch 생성
                    for (Food food : randomFoods) {
                        Lunch lunch = Lunch.builder()
                                .team(team)
                                .food(food)
                                .likeCount(0)
                                .createdAt(LocalDateTime.now())
                                .build();
                        
                        lunchService.createLunch(lunch);
                    }
                    
                    log.info("Created {} lunches for team: {}", randomFoods.size(), team.getName());
                    
                } catch (Exception e) {
                    log.error("Error creating lunches for team {}: {}", team.getName(), e.getMessage());
                }
            }
            
            log.info("Completed lunch creation check");
            
        } catch (Exception e) {
            log.error("Error in lunch creation scheduler: {}", e.getMessage());
        }
    }
}
