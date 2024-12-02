package com.ampersandor.leettrack.controller;

import com.ampersandor.leettrack.common.MyLogger;
import com.ampersandor.leettrack.service.MemberLikeServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/members/like")
public class MemberLikeController {
    
    private final MemberLikeServiceImpl memberLikeService;
    private final ObjectProvider<MyLogger> myLoggerProvider;

    @Autowired
    public MemberLikeController(MemberLikeServiceImpl memberLikeService, ObjectProvider<MyLogger> myLoggerProvider) {
        this.memberLikeService = memberLikeService;
        this.myLoggerProvider = myLoggerProvider;
    }

    @PostMapping("/{memberId}")
    public ResponseEntity<?> addLike(
            @PathVariable("memberId") Long memberId,
            HttpServletRequest request) {
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(request.getRequestURI());
        memberLikeService.addLikeCount(memberId);
        myLogger.log("addLike finished");

        return ResponseEntity.accepted().build();
    }
    
    @GetMapping("/count/{memberId}")
    public ResponseEntity<Long> getLikes(@PathVariable Long memberId, HttpServletRequest request) {
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(request.getRequestURI());
        Long totalLikes = memberLikeService.getLikeCount(memberId);
        myLogger.log("getLikes finished");

        return ResponseEntity.ok(totalLikes);
    }

    @PostMapping("/count")
    public ResponseEntity<Map<Long, Long>> getLikesForMembers(@RequestBody List<Long> memberIds) {
        Map<Long, Long> likeCounts = memberLikeService.getLikeCounts(memberIds);
        return ResponseEntity.ok(likeCounts);
    }

    @GetMapping(path = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @CrossOrigin
    public SseEmitter subscribe(HttpServletRequest request) {
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(request.getRequestURI());
        myLogger.log("subscribe finished");

        return memberLikeService.subscribe();
    }


}