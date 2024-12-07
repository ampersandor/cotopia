package com.ampersandor.cotopia.dto.request;


import java.util.Collections;
import java.util.Map;

public record StatResponse(String status, String message, int totalSolved, int totalQuestions, int easySolved,
                           int totalEasy, int mediumSolved, int totalMedium, int hardSolved, int totalHard,
                           float acceptanceRate, int ranking, int contributionPoints, int reputation,
                           Map<String, Integer> submissionCalendar) {

    @Override
    public String toString() {
        return "StatsResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", totalSolved=" + totalSolved +
                ", totalQuestions=" + totalQuestions +
                ", easySolved=" + easySolved +
                ", totalEasy=" + totalEasy +
                ", mediumSolved=" + mediumSolved +
                ", totalMedium=" + totalMedium +
                ", hardSolved=" + hardSolved +
                ", totalHard=" + totalHard +
                ", acceptanceRate=" + acceptanceRate +
                ", ranking=" + ranking +
                ", contributionPoints=" + contributionPoints +
                ", reputation=" + reputation +
                ", submissionCalendar=" + submissionCalendar +
                '}';
    }

    public static StatResponse error(String status, String message) {
        return new StatResponse(status, message, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0,
                0, 0, Collections.emptyMap());
    }

}