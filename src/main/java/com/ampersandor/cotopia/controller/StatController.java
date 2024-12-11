package com.ampersandor.cotopia.controller;

import com.ampersandor.cotopia.common.MyLogger;
import com.ampersandor.cotopia.dto.StatDTO;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/stats")
public class StatController {

    private final StatService statService;
    private final ObjectProvider<MyLogger> myLoggerProvider;

    @Autowired
    public StatController(StatService statService, ObjectProvider<MyLogger> myLoggerProvider) {
        this.statService = statService;
        this.myLoggerProvider = myLoggerProvider;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<StatDTO>> getStatsByUserBetween(
            @PathVariable("userId") Long userId,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            HttpServletRequest request) {
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(request.getRequestURI());

        List<Stat> stats = statService.getStatsByUserBetween(userId, from, to);
        List<StatDTO> statDTOs = stats.stream()
                .map(StatDTO::from)
                .collect(Collectors.toList());
        myLogger.log("getStatsByUserBetween finished");
        return ResponseEntity.ok(statDTOs);
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<StatDTO>> getStatsByTeamBetween(
            @PathVariable("teamId") Long teamId,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            HttpServletRequest request) {
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(request.getRequestURI());
        
        List<Stat> stats = statService.getStatsByTeamBetween(teamId, from, to);
        List<StatDTO> statDTOs = stats.stream()
                .map(StatDTO::from)
                .collect(Collectors.toList());
                
        myLogger.log("getStatsByTeamBetween finished");

        return ResponseEntity.ok(statDTOs);
    }

}