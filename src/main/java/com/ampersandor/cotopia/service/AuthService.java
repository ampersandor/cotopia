package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.dto.AuthDTO;

public interface AuthService {
    void signup(AuthDTO.SignupRequest request);
    String login(AuthDTO.LoginRequest request);
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
    Long getUserIdFromToken(String token);
} 