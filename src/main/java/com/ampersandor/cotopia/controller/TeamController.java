package com.ampersandor.cotopia.controller;

import com.ampersandor.cotopia.common.MyLogger;
import com.ampersandor.cotopia.dto.TeamRequest;
import com.ampersandor.cotopia.entity.Team;
import com.ampersandor.cotopia.service.TeamService;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/team")
public class TeamController {

    private final TeamService teamService;
    private final ObjectProvider<MyLogger> myLoggerProvider;

    @Autowired
    public TeamController(TeamService teamService, ObjectProvider<MyLogger> myLoggerProvider) {
        this.teamService = teamService;
        this.myLoggerProvider = myLoggerProvider;
    }

    @GetMapping("/get/{teamId}")
    public ResponseEntity<Team> getTeamById(@PathVariable Long teamId, HttpServletRequest request) {
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(request.getRequestURI());
        return teamService.getTeam(teamId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<Team> createTeam(@RequestBody TeamRequest teamRequest, HttpServletRequest request) {
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(request.getRequestURI());
        myLogger.log("createTeam started");
        Team team = Team.builder()
                .name(teamRequest.getName())
                .leaderId(teamRequest.getLeaderId())
                .createdAt(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(teamService.createTeam(team));
    }
}
