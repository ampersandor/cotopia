package com.ampersandor.cotopia.controller;

import com.ampersandor.cotopia.common.MyLogger;
import com.ampersandor.cotopia.dto.TeamDTO;
import com.ampersandor.cotopia.dto.FoodDTO;
import com.ampersandor.cotopia.entity.Team;
import com.ampersandor.cotopia.service.TeamService;
import com.ampersandor.cotopia.service.UserService;
import com.ampersandor.cotopia.service.AuthService;
import com.ampersandor.cotopia.service.FoodService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.ObjectProvider;
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
    private final FoodService foodService;
    private final ObjectProvider<MyLogger> myLoggerProvider;

    @GetMapping("")
    public ResponseEntity<List<TeamDTO.Response>> getTeams(HttpServletRequest request) {
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(request.getRequestURI());
        return ResponseEntity.ok(teamService.getTeams().stream().map(TeamDTO.Response::from).collect(Collectors.toList()));
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamDTO.Response> getTeamById(@PathVariable("teamId") Long teamId, HttpServletRequest request) {
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(request.getRequestURI());
        return teamService.getTeam(teamId)
                .map(team -> ResponseEntity.ok(TeamDTO.Response.from(team)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public ResponseEntity<TeamDTO.Response> createTeam(@RequestBody TeamDTO.CreateRequest teamRequest, @CookieValue(name = "jwt") String token, HttpServletRequest request) {
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(request.getRequestURI());
        myLogger.log("createTeam started");
        
        Long userId = authService.getUserIdFromToken(token);
        Team savedTeam = teamService.createTeam(teamRequest.toEntity(userId));
        userService.updateTeamId(userId, savedTeam.getId());

        return ResponseEntity.ok(TeamDTO.Response.from(savedTeam));
    }

    @PutMapping("/{teamId}")
    public ResponseEntity<TeamDTO.Response> updateTeam(@PathVariable("teamId") Long teamId, @RequestBody TeamDTO.UpdateRequest teamRequest, @CookieValue(name = "jwt") String token, HttpServletRequest request) {
        Long userId = authService.getUserIdFromToken(token);
        Team updatedTeam = teamService.updateTeam(teamRequest.toEntity(), userId);
        return ResponseEntity.ok(TeamDTO.Response.from(updatedTeam));
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<Void> deleteTeam(@PathVariable("teamId") Long teamId, @CookieValue(name = "jwt") String token, HttpServletRequest request) {
        Long userId = authService.getUserIdFromToken(token);
        teamService.deleteTeam(teamId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{teamId}/leave")
    public ResponseEntity<Void> leaveTeam(@PathVariable("teamId") Long teamId, @CookieValue(name = "jwt") String token, HttpServletRequest request) {
        Long userId = authService.getUserIdFromToken(token);
        teamService.leaveTeam(teamId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{teamId}/join")
    public ResponseEntity<Void> joinTeam(@PathVariable("teamId") Long teamId, @CookieValue(name = "jwt") String token, HttpServletRequest request) {
        Long userId = authService.getUserIdFromToken(token);
        teamService.joinTeam(teamId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{teamId}/foods")
    public ResponseEntity<List<FoodDTO.Response>> getFoodsByTeamId(@PathVariable("teamId") Long teamId, HttpServletRequest request) {
        MyLogger myLogger = myLoggerProvider.getObject();
        myLogger.setRequestURL(request.getRequestURI());
        return ResponseEntity.ok(foodService.getFoodsByTeamId(teamId, LocalDate.now()).stream().map(FoodDTO.Response::from).collect(Collectors.toList()));
    }


}
