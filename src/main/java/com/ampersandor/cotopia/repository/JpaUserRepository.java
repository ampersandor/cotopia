package com.ampersandor.cotopia.repository;

import com.ampersandor.cotopia.entity.User;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class JpaUserRepository implements UserRepository {

    private final EntityManager em;

    public JpaUserRepository(EntityManager em) {    
        this.em = em;
    }

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
        return !em.createQuery("select u from ser u where u.email= :email", User.class)
                .setParameter("email", email)
                .getResultList().isEmpty();
    }

}
