package com.ampersandor.cotopia.service;

import com.ampersandor.cotopia.entity.Lunch;
import com.ampersandor.cotopia.entity.Team;

import java.time.LocalDate;
import java.util.List;

public interface LunchService {
    List<Lunch> getLunchesByTeamId(Long teamId, LocalDate date);
    void addLikeCount(Long lunchId, int likeCount);
    Lunch createLunch(Lunch lunch);
    void createLunchesForAllTeams();
    void createLunchesForTeam(Team team);
}