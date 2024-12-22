package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.entity.Food;
import java.util.List;

public interface FoodService {
    Food createFood(Food food);
    Food getFoodById(Long id);
    List<Food> getAllFoods();
    List<Food> getRandomFoods(int count);
    void deleteFood(Long id);
    Food updateFood(Long id, Food food);
    List<Food> searchFoodsByName(String name);
} 