package com.ampersandor.cotopia.service;

import java.util.List;

import com.ampersandor.cotopia.entity.Team;
import com.ampersandor.cotopia.entity.User;
import com.ampersandor.cotopia.repository.TeamRepository;

public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public Team createTeam(Team team) {
        return teamRepository.save(team);
    }

    @Override
    public Team getTeam(Long id) {
        return teamRepository.findById(id).orElseThrow(() -> new RuntimeException("Team not found"));
    }

    @Override
    public List<Team> getTeams() {
        return teamRepository.findAll();
    }

    @Override
    public Team updateTeam(Team team) {
        return teamRepository.save(team);
    }

    @Override
    public void deleteTeam(Long id) {
        teamRepository.deleteById(id);
    }

    @Override
    public void joinTeam(Team team, User user) {
        user.setTeam(team);
        team.getUsers().add(user);
    }

    @Override
    public void leaveTeam(Team team, User user) {
        user.setTeam(null);
        team.getUsers().remove(user);
    }
}
