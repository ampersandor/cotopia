package com.ampersandor.cotopia.repository;

import com.ampersandor.cotopia.entity.Food;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

public interface FoodRepository {
    Food save(Food food);
    void delete(Food food);
    Optional<Food> findById(Long id);
    List<Food> findTodayFoods(Long teamId, LocalDate date);
    Long getLikeCount(Long foodId);
    Food updateLikeCount(Long foodId, int count);
}
