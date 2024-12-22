package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.entity.Food;
import com.ampersandor.cotopia.repository.FoodRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {

    private final FoodRepository foodRepository;

    @Override
    @Transactional
    public Food createFood(Food food) {
        log.info("Creating new food: {}", food.getName());
        return foodRepository.save(food);
    }

    @Override
    @Transactional(readOnly = true)
    public Food getFoodById(Long id) {
        return foodRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Food not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Food> getRandomFoods(int count) {
        log.info("Getting {} random foods", count);
        return foodRepository.findRandomFoods(count);
    }

    @Override
    @Transactional
    public void deleteFood(Long id) {
        log.info("Deleting food with id: {}", id);
        Food food = getFoodById(id);
        foodRepository.delete(food);
    }

    @Override
    @Transactional
    public Food updateFood(Long id, Food updatedFood) {
        log.info("Updating food with id: {}", id);
        Food existingFood = getFoodById(id);
        
        existingFood.setName(updatedFood.getName());
        existingFood.setImgSrc(updatedFood.getImgSrc());
        
        return foodRepository.save(existingFood);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Food> searchFoodsByName(String name) {
        log.info("Searching foods containing name: {}", name);
        return foodRepository.findByNameContaining(name);
    }
} 