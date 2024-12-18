package com.ampersandor.cotopia.repository;

import com.ampersandor.cotopia.entity.Stat;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JpaStatRepository implements StatRepository{

    private final EntityManager em;

    @Override
    public Stat save(Stat stat) {
        em.persist(stat);
        return stat;
    }

    @Override
    public List<Stat> findByAccountIdAndDateBetween(Long accountId, LocalDate from, LocalDate to) {
        return em.createQuery(
                        "SELECT s FROM Stat s WHERE s.codingAccount.id = :accountId AND s.date BETWEEN :from AND :to", Stat.class)
                .setParameter("accountId", accountId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }

    @Override
    public List<Stat> findByUserIdAndDateBetween(Long userId, LocalDate from, LocalDate to) {
        return em.createQuery(
                "SELECT s FROM Stat s " +
                "WHERE s.codingAccount.user.id = :userId " +
                "AND s.date BETWEEN :from AND :to", Stat.class)
                .setParameter("userId", userId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }

    @Override
    public List<Stat> findByTeamIdAndDateBetween(Long teamId, LocalDate from, LocalDate to) {
        return em.createQuery(
                "SELECT s FROM Stat s " +
                "WHERE s.codingAccount.user.team.id = :teamId " +
                "AND s.date BETWEEN :from AND :to", Stat.class)
            .setParameter("teamId", teamId)
            .setParameter("from", from)
            .setParameter("to", to)
            .getResultList();
    }

    @Override
    public boolean existsByAccountIdAndDate(Long accountId, LocalDate date) {
        return em.createQuery("SELECT COUNT(s) FROM Stat s WHERE s.codingAccount.id = :accountId AND s.date = :date", Long.class)
                .setParameter("accountId", accountId)
                .setParameter("date", date)
                .getSingleResult() > 0;
    }



}
