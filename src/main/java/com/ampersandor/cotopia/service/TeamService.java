package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.entity.Team;

import java.util.List;
import java.util.Optional;

public interface TeamService {
    Team createTeam(Team team);
    Optional<Team> getTeam(Long id);
    List<Team> getTeams();
    Team updateTeam(Team team, Long userId);
    void deleteTeam(Long teamId, Long userId);
    void joinTeam(Long teamId, Long userId);
    void leaveTeam(Long teamId, Long userId);
}