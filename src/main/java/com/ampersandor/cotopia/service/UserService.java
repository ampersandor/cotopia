package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.dto.LoginRequest;
import com.ampersandor.cotopia.dto.SignupRequest;
import com.ampersandor.cotopia.entity.User;

import java.util.Optional;

public interface UserService {
    void signup(SignupRequest request);
    String login(LoginRequest request);
    Optional<User> findOne(Long id);
    Optional<User> findByUsername(String username);
    void updateTeamId(Long userId, Long teamId);
    void deleteTeamId(Long userId);
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
}
