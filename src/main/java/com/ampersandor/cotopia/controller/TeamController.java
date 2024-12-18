package com.ampersandor.cotopia.controller;

import com.ampersandor.cotopia.common.MyLogger;
import com.ampersandor.cotopia.dto.TeamDTO;
import com.ampersandor.cotopia.dto.FoodDTO;
import com.ampersandor.cotopia.entity.Team;
import com.ampersandor.cotopia.entity.Food;
import com.ampersandor.cotopia.service.TeamService;
import com.ampersandor.cotopia.service.UserService;
import com.ampersandor.cotopia.service.AuthService;
import com.ampersandor.cotopia.service.FoodService;
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
    private final FoodService foodService;
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

    @GetMapping("/{teamId}/foods")
    public ResponseEntity<List<FoodDTO.Response>> getFoodsByTeamId(@PathVariable("teamId") Long teamId) {
        myLogger.log("getFoodsByTeamId started");
        List<Food> foods = foodService.getFoodsByTeamId(teamId, LocalDate.now());
        myLogger.log("getFoodsByTeamId finished");
        return ResponseEntity.ok(foods.stream().map(FoodDTO.Response::from).collect(Collectors.toList()));
    }


}
