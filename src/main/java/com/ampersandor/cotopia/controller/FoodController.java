package com.ampersandor.cotopia.controller;

import com.ampersandor.cotopia.common.MyLogger;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import com.ampersandor.cotopia.service.FoodService;

import java.util.Map;

@RestController
@RequestMapping("/api/foods")
public class FoodController {
    
    private final FoodService foodService;
    private final ObjectProvider<MyLogger> myLoggerProvider;

    @Autowired
    public FoodController(FoodService foodService, ObjectProvider<MyLogger> myLoggerProvider) {
        this.foodService = foodService;
        this.myLoggerProvider = myLoggerProvider;
    }

    @PostMapping("/add/{teamId}/{foodId}/{likeCount}")
    public ResponseEntity<?> addLike(
            @PathVariable("teamId") Long teamId,
            @PathVariable("foodId") Long foodId,
            @PathVariable("likeCount") Integer likeCount,
            HttpServletRequest request) {
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(request.getRequestURI());
        foodService.addLikeCount(teamId, foodId, likeCount);
        myLogger.log("addLike finished");

        return ResponseEntity.accepted().build();
    }
    
    @GetMapping("/get/{teamId}")
    public ResponseEntity<Map<Long, Integer>> getLikes(@PathVariable Long teamId, HttpServletRequest request) {
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(request.getRequestURI());
        Map<Long, Integer> totalLikes = foodService.getLikeCounts(teamId);
        myLogger.log("getLikes finished");

        return ResponseEntity.ok(totalLikes);
    }

    @GetMapping(path = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @CrossOrigin
    public SseEmitter subscribe(HttpServletRequest request) {
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(request.getRequestURI());
        myLogger.log("subscribe finished");

        return foodService.subscribe();
    }


}