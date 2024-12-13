package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findOne(Long id);
    Optional<User> findByUsername(String username);
    void updateTeamId(Long userId, Long teamId);
    void deleteTeamId(Long userId);
}