package com.ampersandor.cotopia.service;

import java.util.List;
import java.util.Optional;

import com.ampersandor.cotopia.entity.Team;
import com.ampersandor.cotopia.entity.User;
import com.ampersandor.cotopia.repository.TeamRepository;

import org.springframework.transaction.annotation.Transactional;

@Transactional
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
    public Optional<Team> getTeam(Long id) {
        return teamRepository.findById(id);
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
