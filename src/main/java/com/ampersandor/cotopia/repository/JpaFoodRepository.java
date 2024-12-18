package com.ampersandor.cotopia.repository;

import com.ampersandor.cotopia.entity.Food;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JpaFoodRepository implements FoodRepository {

    private final EntityManager em;

    @Override
    public Food save(Food food) {
        em.persist(food);
        return food;
    }

    @Override
    public void deleteById(Long id) {
        em.remove(em.find(Food.class, id));
    }

    @Override
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

    @Override
    public List<Food> findByTeamIdAndCreatedAtDate(Long teamId, LocalDate date) {
        return em.createQuery(
                "SELECT f FROM Food f WHERE f.team.id = :teamId AND f.date = :date", Food.class)
                .setParameter("teamId", teamId)
                .setParameter("date", date)
                .getResultList();
    }

    @Override
    public void addLikeCount(Long foodId, int likeCount) {
        Food food = em.find(Food.class, foodId);
        food.setLikeCount(food.getLikeCount() + likeCount);
        em.merge(food);
    }
}
