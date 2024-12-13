package com.ampersandor.cotopia.service;

import java.util.List;
import java.util.Optional;

import com.ampersandor.cotopia.entity.Team;
import com.ampersandor.cotopia.entity.User;
import com.ampersandor.cotopia.repository.TeamRepository;
import com.ampersandor.cotopia.repository.UserRepository;


import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;

@Transactional
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public TeamServiceImpl(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Team createTeam(Team team) {
        User leader = userRepository.findById(team.getLeaderId())
                .orElseThrow(() -> new EntityNotFoundException("Leader not found"));
        
        leader.setTeam(team);
        
        return teamRepository.save(team);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Team> getTeam(Long id) {
        return teamRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Team> getTeams() {
        return teamRepository.findAll();
    }

    @Override
    public Team updateTeam(Team team, Long userId) {
        Team existingTeam = teamRepository.findById(team.getId())
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));
        
        if (!existingTeam.getLeaderId().equals(userId)) {
            throw new IllegalStateException("User is not the leader of this team");
        }

        existingTeam.setName(team.getName());
        
        return teamRepository.save(existingTeam);
    }   

    @Override
    public void deleteTeam(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));
        
        if (!team.getLeaderId().equals(userId)) {
            throw new IllegalStateException("User is not the leader of this team");
        }
        
        team.getUsers().forEach(user -> user.setTeam(null));
        
        teamRepository.deleteById(teamId);
    }

    @Override
    public void joinTeam(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.getTeam() != null) {
            throw new IllegalStateException("User already belongs to a team");
        }
        
        user.setTeam(team);
        team.getUsers().add(user);
    }

    @Override
    public void leaveTeam(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (team.getLeaderId().equals(userId)) {
            throw new IllegalStateException("Team leader cannot leave the team");
        }
        
        if (!team.equals(user.getTeam())) {
            throw new IllegalStateException("User is not a member of this team");
        }

        user.setTeam(null);
        team.getUsers().remove(user);
    }
}
