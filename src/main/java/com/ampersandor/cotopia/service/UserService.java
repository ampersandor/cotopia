package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.dto.LoginRequest;
import com.ampersandor.cotopia.dto.SignupRequest;
import com.ampersandor.cotopia.entity.User;

import java.util.Optional;

public interface UserService {
    void signup(SignupRequest request);
    String login(LoginRequest request);
    Optional<User> findOne(Long id);
}
