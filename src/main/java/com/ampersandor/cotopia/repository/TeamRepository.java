package com.ampersandor.cotopia.repository;

import java.util.List;
import java.util.Optional;

import com.ampersandor.cotopia.entity.Team;

public interface TeamRepository {
    Team save(Team team);
    Optional<Team> findById(Long id);
    List<Team> findAll();
    List<Team> findByUserId(Long userId);
    void deleteById(Long id);
}
