package com.ampersandor.cotopia.repository;

import com.ampersandor.cotopia.entity.User;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JpaUserRepository implements UserRepository {

    private final EntityManager em;

    @Override
    public User save(User user) {
        em.persist(user);
        return user;
    }

    @Override
    public void delete(User user) {
        em.remove(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        User user = em.find(User.class, id);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        List<User> result = em.createQuery("select u from User u where u.username= :username", User.class)
                .setParameter("username", username)
                .getResultList();

        return result.stream().findAny();
    }

    @Override
    public boolean existsByUsername(String username) {
        return !em.createQuery("select u from User u where u.username= :username", User.class)
                .setParameter("username", username)
                .getResultList().isEmpty();
    }

    @Override
    public boolean existsByEmail(String email) {
        return !em.createQuery("select u from User u where u.email= :email", User.class)
                .setParameter("email", email)
                .getResultList().isEmpty();
    }

    @Override
    public void updateTeamId(Long userId, Long teamId) {
        em.createQuery("UPDATE User u SET u.team.id = :teamId WHERE u.id = :userId")
            .setParameter("teamId", teamId)
            .setParameter("userId", userId)
            .executeUpdate();
    }

    @Override
    public void deleteTeamId(Long userId) {
        em.createQuery("UPDATE User u SET u.team.id = NULL WHERE u.id = :userId")
            .setParameter("userId", userId)
            .executeUpdate();
    }


}
