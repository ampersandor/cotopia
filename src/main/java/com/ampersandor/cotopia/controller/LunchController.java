package com.ampersandor.cotopia.controller;

import org.springframework.web.bind.annotation.*;
import com.ampersandor.cotopia.service.LunchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lunches")
public class LunchController {
    
    private final LunchService lunchService;




}