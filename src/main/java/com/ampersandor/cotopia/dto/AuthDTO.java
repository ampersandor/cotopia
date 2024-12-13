package com.ampersandor.cotopia.dto;

import com.ampersandor.cotopia.entity.User;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthDTO {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SignupRequest {
        @NotBlank(message = "Username cannot be empty")
        private String username;
        @NotBlank(message = "Password cannot be empty")
        private String password;
        @NotBlank(message = "Email cannot be empty")
        private String email;

        public User toEntity() {
            return User.builder()
                    .username(this.username)
                    .password(this.password)
                    .email(this.email)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SignupResponse {
        private String username;

        public static SignupResponse from(SignupRequest request) {
            return SignupResponse.builder()
                    .username(request.getUsername())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginRequest {
        @NotBlank(message = "Username cannot be empty")
        private String username;
        @NotBlank(message = "Password cannot be empty")
        private String password;

        public User toEntity() {
            return User.builder()
                    .username(this.username)
                    .password(this.password)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginResponse {
        private String token;

        public static LoginResponse from(String token) {
            return LoginResponse.builder()
                    .token(token)
                    .build();
        }
    }
}
