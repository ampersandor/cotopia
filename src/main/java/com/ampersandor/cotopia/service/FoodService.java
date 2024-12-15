package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.entity.Food;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface FoodService {
    List<Food> getFoodsByTeamId(Long teamId, LocalDate date);
    // Food save(Food food);
    // void delete(Long id);
    // Optional<Food> findById(Long id);
    void addLikeCount(Long foodId, int likeCount);
}