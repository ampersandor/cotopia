package com.ampersandor.cotopia.repository;

import com.ampersandor.cotopia.entity.Food;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

public interface FoodRepository {
    Food save(Food food);
    void deleteById(Long id);
    Optional<Food> findById(Long id);
    List<Food> findByTeamIdAndCreatedAtDate(Long teamId, LocalDate date);
    void addLikeCount(Long foodId, int likeCount);
}
