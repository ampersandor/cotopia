package com.ampersandor.cotopia.controller;

import com.ampersandor.cotopia.common.MyLogger;
import com.ampersandor.cotopia.entity.Stat;
import com.ampersandor.cotopia.service.StatService;
import com.ampersandor.cotopia.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/stat")
public class StatController {

    private final StatService statService;
    private final UserService userService;
    private final ObjectProvider<MyLogger> myLoggerProvider;

    @Autowired
    public StatController(StatService statService, UserService userService, ObjectProvider<MyLogger> myLoggerProvider) {
        this.statService = statService;
        this.userService = userService;
        this.myLoggerProvider = myLoggerProvider;
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<List<Stat>> getStatsByUserBetween(
            @PathVariable("userId") Long userId,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            HttpServletRequest request) {
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(request.getRequestURI());


        return userService.findOne(userId)
                .map(user -> {
                    List<Stat> stats = statService.getStatsByUserBetween(user, from, to);
                    myLogger.log("getStatsByUserBetween finished");
                    return ResponseEntity.ok(stats);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<Stat>> getStatsByTeamBetween(
            @RequestParam("teamId") Long teamId,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            HttpServletRequest request) {
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(request.getRequestURI());
        List<Stat> res = statService.getStatsByTeamBetween(teamId, from, to);
        myLogger.log("getStatsByTeamBetween finished");

        return ResponseEntity.ok(res);
    }

}