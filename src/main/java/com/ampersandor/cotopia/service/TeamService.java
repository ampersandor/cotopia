package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.entity.Team;
import com.ampersandor.cotopia.entity.User;

import java.util.List;
import java.util.Optional;

public interface TeamService {
    Team createTeam(Team team);
    Optional<Team> getTeam(Long id);
    List<Team> getTeams();
    Team updateTeam(Team team);
    void deleteTeam(Long id);
    void joinTeam(Team team, User user);
    void leaveTeam(Team team, User user);
}
