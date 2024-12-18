package com.ampersandor.cotopia.controller;


import com.ampersandor.cotopia.service.AuthService;
import com.ampersandor.cotopia.service.UserService;
import com.ampersandor.cotopia.entity.User;
import com.ampersandor.cotopia.dto.UserDTO;
import com.ampersandor.cotopia.common.MyLogger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CookieValue;



import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final MyLogger myLogger;

    @GetMapping("/me")
    public ResponseEntity<UserDTO.Response> getUser(@CookieValue("jwt") String token) {
        myLogger.log("getUser started");
        Long userId = authService.getUserIdFromToken(token);
        User user = userService.findOne(userId).orElseThrow(() -> new RuntimeException("User not found"));
        myLogger.log("getUser finished");
        return ResponseEntity.ok(UserDTO.Response.from(user));
    } 
   
}
