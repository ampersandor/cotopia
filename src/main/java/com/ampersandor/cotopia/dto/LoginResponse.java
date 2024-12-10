package com.ampersandor.cotopia.dto;

public class LoginResponse {
    private String message;
    private String token;
    private Long userId;
    private String username;

    // 기본 생성자
    public LoginResponse() {
    }

    // 간단한 메시지만 포함하는 생성자
    public LoginResponse(String message) {
        this.message = message;
    }

    // 모든 필드를 포함하는 생성자
    public LoginResponse(String message, String token, Long userId, String username) {
        this.message = message;
        this.token = token;
        this.userId = userId;
        this.username = username;
    }

    // Getters
    public String getMessage() { return message; }
    public String getToken() { return token; }
    public Long getUserId() { return userId; }
    public String getUsername() { return username; }

    // Setters
    public void setMessage(String message) { this.message = message; }
    public void setToken(String token) { this.token = token; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }
}