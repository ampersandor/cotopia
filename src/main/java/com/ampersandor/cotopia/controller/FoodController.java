package com.ampersandor.cotopia.controller;

import com.ampersandor.cotopia.dto.FoodDTO;
import com.ampersandor.cotopia.entity.Food;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ampersandor.cotopia.service.FoodService;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/foods")
public class FoodController {
    
    private final FoodService foodService;




}