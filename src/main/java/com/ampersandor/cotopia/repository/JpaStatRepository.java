package com.ampersandor.cotopia.repository;

import com.ampersandor.cotopia.model.Member;
import com.ampersandor.cotopia.model.Stat;
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
    public List<Stat> findByIdInRange(Member member, LocalDate from, LocalDate to) {
        return em.createQuery(
                        "SELECT s FROM Stat s WHERE s.memberId = :memberId AND s.date BETWEEN :from AND :to", Stat.class)
                .setParameter("memberId", member.getId())
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
    public List<Stat> findByMember(Member member) {
        return em.createQuery(
                "SELECT s FROM Stat s WHERE s.memberId = :memberId", Stat.class)
                .setParameter("memberId", member.getId())
                .getResultList();

    }
}
