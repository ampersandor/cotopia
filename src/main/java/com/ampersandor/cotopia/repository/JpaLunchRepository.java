package com.ampersandor.cotopia.repository;

import com.ampersandor.cotopia.entity.Lunch;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JpaLunchRepository implements LunchRepository {

    private final EntityManager em;

    @Override
    public Lunch save(Lunch lunch) {
        em.persist(lunch);
        return lunch;
    }

    @Override
    public void deleteById(Long id) {
        em.remove(em.find(Lunch.class, id));
    }

    @Override
    public Optional<Lunch> findById(Long id) {
        try {
            Lunch result = em.createQuery(
                            "SELECT l FROM Lunch l WHERE l.id = :id",
                            Lunch.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return Optional.of(result);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Lunch> findByTeamIdAndCreatedAtDate(Long teamId, LocalDate date) {
        return em.createQuery(
                "SELECT l FROM Lunch l WHERE l.team.id = :teamId AND l.date = :date", Lunch.class)
                .setParameter("teamId", teamId)
                .setParameter("date", date)
                .getResultList();
    }

    @Override
    public void addLikeCount(Long lunchId, int likeCount) {
        Lunch lunch = em.find(Lunch.class, lunchId);
        lunch.setLikeCount(lunch.getLikeCount() + likeCount);
        em.merge(lunch);
    }
}
