package com.ampersandor.cotopia.controller;

import org.springframework.web.bind.annotation.*;
import com.ampersandor.cotopia.service.FoodService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/foods")
public class FoodController {
    
    private final FoodService foodService;




}