package com.ampersandor.cotopia.repository;

import com.ampersandor.cotopia.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    
    @Query(value = "SELECT * FROM foods ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<Food> findRandomFoods(@Param("limit") int limit);
    
    List<Food> findByNameContaining(String name);
    
    @Query("SELECT f FROM Food f WHERE f.id IN :ids")
    List<Food> findByIds(@Param("ids") List<Long> foodIds);
}
