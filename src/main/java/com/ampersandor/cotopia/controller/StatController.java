package com.ampersandor.cotopia.controller;

import com.ampersandor.cotopia.common.MyLogger;
import com.ampersandor.cotopia.dto.StatDTO;
import com.ampersandor.cotopia.entity.Stat;
import com.ampersandor.cotopia.service.StatService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stats")
public class StatController {

    private final StatService statService;
    private final ObjectProvider<MyLogger> myLoggerProvider;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<StatDTO.Response>> getStatsByUserBetween(
            @PathVariable("userId") Long userId,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            HttpServletRequest request) {
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(request.getRequestURI());

        List<Stat> stats = statService.getStatsByUserBetween(userId, from, to);
        List<StatDTO.Response> statDTOs = stats.stream()
                .map(StatDTO.Response::from)
                .collect(Collectors.toList());
        myLogger.log("getStatsByUserBetween finished");
        return ResponseEntity.ok(statDTOs);
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<StatDTO.Response>> getStatsByTeamBetween(
            @PathVariable("teamId") Long teamId,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            HttpServletRequest request) {
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(request.getRequestURI());
        
        List<Stat> stats = statService.getStatsByTeamBetween(teamId, from, to);
        List<StatDTO.Response> statDTOs = stats.stream()
                .map(StatDTO.Response::from)
                .collect(Collectors.toList());
                
        myLogger.log("getStatsByTeamBetween finished");

        return ResponseEntity.ok(statDTOs);
    }

}