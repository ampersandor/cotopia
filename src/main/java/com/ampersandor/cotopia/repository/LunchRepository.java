package com.ampersandor.cotopia.repository;

import com.ampersandor.cotopia.entity.Lunch;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

public interface LunchRepository {
    Lunch save(Lunch lunch);
    void deleteById(Long id);
    Optional<Lunch> findById(Long id);
    List<Lunch> findByTeamIdAndCreatedAtDate(Long teamId, LocalDate date);
    void addLikeCount(Long lunchId, int likeCount);
}
