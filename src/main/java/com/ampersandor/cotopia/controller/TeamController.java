package com.ampersandor.cotopia.controller;

import com.ampersandor.cotopia.common.MyLogger;
import com.ampersandor.cotopia.dto.TeamDTO;
import com.ampersandor.cotopia.dto.LunchDTO;
import com.ampersandor.cotopia.entity.Team;
import com.ampersandor.cotopia.entity.Lunch;
import com.ampersandor.cotopia.service.TeamService;
import com.ampersandor.cotopia.service.UserService;
import com.ampersandor.cotopia.service.AuthService;
import com.ampersandor.cotopia.service.LunchService;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams")
public class TeamController {

    private final TeamService teamService;
    private final UserService userService;
    private final AuthService authService;
    private final LunchService lunchService;
    private final MyLogger myLogger;

    @GetMapping("")
    public ResponseEntity<List<TeamDTO.Response>> getTeams() {
        myLogger.log("getTeams started");
        List<Team> teams = teamService.getTeams();
        myLogger.log("getTeams finished");
        return ResponseEntity.ok(teams.stream().map(TeamDTO.Response::from).collect(Collectors.toList()));
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamDTO.Response> getTeamById(@PathVariable("teamId") Long teamId) {
        myLogger.log("getTeamById started");
        Team team = teamService.getTeam(teamId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found"));
        myLogger.log("getTeamById finished");
        return ResponseEntity.ok(TeamDTO.Response.from(team));
    }

    @PostMapping("")
    public ResponseEntity<TeamDTO.Response> createTeam(
        @RequestBody TeamDTO.CreateRequest teamRequest, 
        @CookieValue(name = "jwt") String token) {
        myLogger.log("createTeam started");
        
        Long userId = authService.getUserIdFromToken(token);
        Team savedTeam = teamService.createTeam(teamRequest.toEntity(userId));
        userService.updateTeamId(userId, savedTeam.getId());

        // 새 팀에 대한 Lunch 생성
        lunchService.createLunchesForTeam(savedTeam);

        myLogger.log("createTeam finished");
        return ResponseEntity.ok(TeamDTO.Response.from(savedTeam));
    }

    @PutMapping("/{teamId}")
    public ResponseEntity<TeamDTO.Response> updateTeam(
        @PathVariable("teamId") Long teamId, 
        @RequestBody TeamDTO.UpdateRequest teamRequest, 
        @CookieValue(name = "jwt") String token) {
        myLogger.log("updateTeam started");
        Long userId = authService.getUserIdFromToken(token);
        Team updatedTeam = teamService.updateTeam(teamRequest.toEntity(), userId);
        myLogger.log("updateTeam finished");
        return ResponseEntity.ok(TeamDTO.Response.from(updatedTeam));
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<Void> deleteTeam(@PathVariable("teamId") Long teamId, @CookieValue(name = "jwt") String token) {
        myLogger.log("deleteTeam started");
        Long userId = authService.getUserIdFromToken(token);
        teamService.deleteTeam(teamId, userId);
        myLogger.log("deleteTeam finished");
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{teamId}/leave")
    public ResponseEntity<Void> leaveTeam(@PathVariable("teamId") Long teamId, @CookieValue(name = "jwt") String token) {
        myLogger.log("leaveTeam started");
        Long userId = authService.getUserIdFromToken(token);
        teamService.leaveTeam(teamId, userId);
        myLogger.log("leaveTeam finished");
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{teamId}/join")
    public ResponseEntity<Void> joinTeam(@PathVariable("teamId") Long teamId, @CookieValue(name = "jwt") String token) {
        myLogger.log("joinTeam started");
        Long userId = authService.getUserIdFromToken(token);
        teamService.joinTeam(teamId, userId);
        myLogger.log("joinTeam finished");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{teamId}/lunches")
    public ResponseEntity<List<LunchDTO.Response>> getLunchesByTeamId(@PathVariable("teamId") Long teamId) {
        myLogger.log("getLunchesByTeamId started");
        List<Lunch> lunches = lunchService.getLunchesByTeamId(teamId, LocalDate.now());
        myLogger.log("getLunchesByTeamId finished");
        return ResponseEntity.ok(lunches.stream().map(LunchDTO.Response::from).collect(Collectors.toList()));
    }


}
