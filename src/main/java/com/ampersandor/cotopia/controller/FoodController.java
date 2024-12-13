package com.ampersandor.cotopia.controller;

import com.ampersandor.cotopia.common.MyLogger;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import com.ampersandor.cotopia.service.FoodService;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/foods")
public class FoodController {
    
    private final FoodService foodService;
    private final ObjectProvider<MyLogger> myLoggerProvider;

    @PostMapping("/team/{teamId}/food/{foodId}/likes")
    public ResponseEntity<?> addLike(
            @PathVariable("teamId") Long teamId,
            @PathVariable("foodId") Long foodId,
            @RequestBody Map<String, Integer> request,  // 또는 별도의 DTO 클래스 사용
            HttpServletRequest httpRequest) {
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(httpRequest.getRequestURI());
        foodService.addLikeCount(teamId, foodId, request.get("likeCount"));
        myLogger.log("addLike finished");

        return ResponseEntity.accepted().build();
    }
    
    @GetMapping("/team/{teamId}/likes")
    public ResponseEntity<Map<Long, Integer>> getLikes(@PathVariable("teamId") Long teamId, HttpServletRequest httpRequest) {
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(httpRequest.getRequestURI());
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