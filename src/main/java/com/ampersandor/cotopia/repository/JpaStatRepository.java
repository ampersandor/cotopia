package com.ampersandor.cotopia.repository;

import com.ampersandor.cotopia.entity.Stat;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

public class JpaStatRepository implements StatRepository{

    private final EntityManager em;

    public JpaStatRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Stat save(Stat stat) {
        em.persist(stat);
        return stat;
    }

    @Override
    public List<Stat> findByAccountIdAndDateBetween(Long accountId, LocalDate from, LocalDate to) {
        return em.createQuery(
                        "SELECT s FROM Stat s WHERE s.account_id = :accountId AND s.date BETWEEN :from AND :to", Stat.class)
                .setParameter("accountId", accountId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }

    @Override
    public List<Stat> findAll() {
        return em.createQuery("select s from Stat s", Stat.class)
                .getResultList();
    }


    @Override
    public List<Stat> findByAccountIdAndDate(Long accountId, LocalDate date) {
        return em.createQuery("SELECT s FROM Stat s WHERE s.account_id = :accountId AND s.date = :date", Stat.class)
                .setParameter("accountId", accountId)
                .setParameter("date", date)
                .getResultList();
    }

    @Override
    public boolean existsByAccountIdAndDate(Long accountId, LocalDate date) {
        return em.createQuery("SELECT COUNT(s) FROM Stat s WHERE s.codingAccount.id = :accountId AND s.date = :date", Long.class)
                .setParameter("accountId", accountId)
                .setParameter("date", date)
                .getSingleResult() > 0;
    }   

    @Override
    public List<Stat> findByUserIdAndDateBetween(Long userId, LocalDate from, LocalDate to) {
        return em.createQuery("SELECT s FROM Stat s WHERE s.user_id = :userId AND s.date BETWEEN :from AND :to", Stat.class)
                .setParameter("userId", userId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getResultList();
    }

    @Override
    public List<Stat> findByUserIdAndDate(Long userId, LocalDate date) {
        return em.createQuery("SELECT s FROM Stat s WHERE s.user_id = :userId AND s.date = :date", Stat.class)
                .setParameter("userId", userId)
                .setParameter("date", date)
                .getResultList();
    }

    @Override
    public List<Stat> findByTeamIdAndDateBetween(Long teamId, LocalDate from, LocalDate to) {
        return em.createQuery(
                "SELECT s FROM Stat s " +
                "JOIN User u ON s.user_id = u.id " +
                "JOIN u.team t " +
                "WHERE t.id = :teamId AND s.date BETWEEN :from AND :to", Stat.class)
            .setParameter("teamId", teamId)
            .setParameter("from", from)
            .setParameter("to", to)
            .getResultList();
    }

}
