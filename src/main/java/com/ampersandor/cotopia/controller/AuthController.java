package com.ampersandor.cotopia.controller;

import com.ampersandor.cotopia.dto.AuthDTO;
import com.ampersandor.cotopia.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ampersandor.cotopia.common.MyLogger;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final MyLogger myLogger;

    @PostMapping("/signup")
    public ResponseEntity<AuthDTO.SignupResponse> signup(@Valid @RequestBody AuthDTO.SignupRequest request) {
        myLogger.log("signup started");
        authService.signup(request);
        myLogger.log("signup finished");
        return ResponseEntity.ok(AuthDTO.SignupResponse.from(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDTO.LoginResponse> login(@Valid @RequestBody AuthDTO.LoginRequest request) {
        myLogger.log("login started");
        String token = authService.login(request);
        myLogger.log("login finished");
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(24 * 60 * 60)
            .build();
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(AuthDTO.LoginResponse.from(token));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        myLogger.log("logout started");
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(0)
            .sameSite("Strict")
            .build();
        myLogger.log("logout finished");
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(Map.of("message", "Logged out successfully"));
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validate(@CookieValue(name = "jwt") String token) {
        myLogger.log("validate started");
        boolean isValid = authService.validateToken(token);
        myLogger.log("validate finished");
        return ResponseEntity.ok(isValid);
    }

}
