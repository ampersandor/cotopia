package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.entity.Team;
import com.ampersandor.cotopia.entity.User;

import java.util.List;

public interface TeamService {
    Team createTeam(Team team);
    Team getTeam(Long id);
    List<Team> getTeams();
    Team updateTeam(Team team);
    void deleteTeam(Long id);
    void joinTeam(Team team, User user);
    void leaveTeam(Team team, User user);
}
