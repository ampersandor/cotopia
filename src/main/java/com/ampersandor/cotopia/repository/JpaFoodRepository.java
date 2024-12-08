package com.ampersandor.cotopia.repository;

import com.ampersandor.cotopia.entity.Food;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

@Repository
public class JpaFoodRepository implements FoodRepository {

    private final EntityManager em;

    public JpaFoodRepository(EntityManager em) {
        this.em = em;
    }
   
    public Food save(Food food) {
        em.persist(food);
        return food;
    }

    public void delete(Food food) {
        em.remove(food);
    }
    public Optional<Food> findById(Long id) {
        try {
            Food result = em.createQuery(
                            "SELECT f FROM Food f WHERE f.id = :id",
                            Food.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return Optional.of(result);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public List<Food> findTodayFoods(Long teamId, LocalDate date) {
        return em.createQuery(
                "SELECT f FROM Food f WHERE f.teamId = :teamId AND f.date = :date", Food.class)
                .setParameter("teamId", teamId)
                .setParameter("date", date)
                .getResultList();
    }

    public Long getLikeCount(Long foodId) {
        try {
            return em.createQuery(
                            "SELECT f.likeCount FROM Food f WHERE f.id = :foodId",
                            Long.class)
                    .setParameter("foodId", foodId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return 0L;
        }
    }

    public Food updateLikeCount(Long foodId, int count) {
        Food food = findById(foodId)
                .orElseThrow(() -> new RuntimeException("Food not found with id: " + foodId));
        food.setLikes(food.getLikes() + count);
        return save(food);
    }
}
