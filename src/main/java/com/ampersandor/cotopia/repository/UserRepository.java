package com.ampersandor.cotopia.repository;

import com.ampersandor.cotopia.entity.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    void delete(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
