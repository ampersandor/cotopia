package com.ampersandor.cotopia.dto;

import java.util.Collections;
import java.util.Map;

public record StatResponse(
    String status,
    String message,
    int totalSolved,
    int totalQuestions,
    int easySolved,
    int totalEasy,
    int mediumSolved,
    int totalMedium,
    int hardSolved,
    int totalHard,
    float acceptanceRate,
    int ranking,
    int contributionPoints,
    int reputation,
    Map<String, Integer> submissionCalendar
) {
    public static StatResponse error(String status, String message) {
        return new StatResponse(
            status, 
            message, 
            0, 0, 0, 0, 0, 0, 0, 0, 
            0, 0, 0, 0, 
            Collections.emptyMap()
        );
    }
} 