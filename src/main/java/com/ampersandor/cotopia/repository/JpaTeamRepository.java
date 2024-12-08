package com.ampersandor.cotopia.repository;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import com.ampersandor.cotopia.entity.Team;

public class JpaTeamRepository implements TeamRepository {

    private final EntityManager em;

    public JpaTeamRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Team save(Team team) {
        em.persist(team);
        return team;
    }

    @Override
    public Optional<Team> findById(Long id) {
        return Optional.ofNullable(em.find(Team.class, id));
    }

    @Override
    public List<Team> findAll() {
        return em.createQuery("SELECT t FROM Team t", Team.class).getResultList();
    }

    @Override
    public List<Team> findByUserId(Long userId) {
        return em.createQuery("SELECT t FROM Team t WHERE t.userId = :userId", Team.class).setParameter("userId", userId).getResultList();
    }

    @Override
    public void deleteById(Long id) {
        em.remove(em.find(Team.class, id));
    }
}
