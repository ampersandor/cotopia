package com.ampersandor.cotopia.controller;

import com.ampersandor.cotopia.common.MyLogger;
import com.ampersandor.cotopia.dto.TeamDTO;
import com.ampersandor.cotopia.dto.TeamRequest;
import com.ampersandor.cotopia.entity.Team;
import com.ampersandor.cotopia.service.TeamService;
import com.ampersandor.cotopia.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/team")
public class TeamController {

    private final TeamService teamService;
    private final UserService userService;
    private final ObjectProvider<MyLogger> myLoggerProvider;

    @Autowired
    public TeamController(TeamService teamService, UserService userService, ObjectProvider<MyLogger> myLoggerProvider) {
        this.teamService = teamService;
        this.userService = userService;
        this.myLoggerProvider = myLoggerProvider;
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable("teamId") Long teamId, HttpServletRequest request) {
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(request.getRequestURI());
        return teamService.getTeam(teamId)
                .map(team -> ResponseEntity.ok(TeamDTO.from(team)))
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
        Team savedTeam = teamService.createTeam(team);
        userService.updateTeamId(teamRequest.getLeaderId(), savedTeam.getId());

        return ResponseEntity.ok(savedTeam);
    }

    @DeleteMapping("/delete/{teamId}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long teamId, HttpServletRequest request) {
        teamService.deleteTeam(teamId);
        return ResponseEntity.noContent().build();
    }
}
