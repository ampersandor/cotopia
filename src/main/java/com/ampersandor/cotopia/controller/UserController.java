package com.ampersandor.cotopia.controller;

import com.ampersandor.cotopia.dto.LoginRequest;
import com.ampersandor.cotopia.dto.SignupRequest;
import com.ampersandor.cotopia.dto.LoginResponse;
import com.ampersandor.cotopia.service.UserService;
import com.ampersandor.cotopia.entity.User;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CookieValue;

import java.util.Map;
import java.util.Optional;
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        userService.signup(request);
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String jwtToken = userService.login(loginRequest);
        
        ResponseCookie cookie = ResponseCookie.from("jwt", jwtToken)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(3600)
            .sameSite("Strict")
            .build();
        
        Optional<User> user = userService.findByUsername(loginRequest.getUsername());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid username or password"));
        }
        LoginResponse loginResponse = new LoginResponse(
            "Login successful",
            jwtToken,
            user.get().getId(),
            user.get().getUsername()
        );
        
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(loginResponse);
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkToken(@CookieValue(name = "jwt", required = false) String token) {
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("valid", false, "message", "No token found"));
        }

        try {
            // 토큰 유효성 검사
            if (userService.validateToken(token)) {
                String username = userService.getUsernameFromToken(token);
                return ResponseEntity.ok(Map.of(
                    "valid", true,
                    "username", username,
                    "message", "Token is valid"
                ));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("valid", false, "message", "Invalid token"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("valid", false, "message", "Token validation failed"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // 쿠키 무효화 (만료시간을 0으로 설정)
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(0)  // 즉시 만료
            .sameSite("Strict")
            .build();
        
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(Map.of("message", "Logged out successfully"));
    }
}