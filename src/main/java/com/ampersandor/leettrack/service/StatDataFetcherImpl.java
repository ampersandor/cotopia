package com.ampersandor.leettrack.service;

import com.ampersandor.leettrack.model.StatResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.TreeMap;

public class StatDataFetcherImpl implements StatDataFetcher{
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public StatDataFetcherImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public StatResponse fetchStat(String username) {
        String url = "https://leetcode.com/graphql/";

        HttpEntity<String> entity = getStringHttpEntity(username);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, String.class
            );

            String responseString = response.getBody();

            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode rootNode = objectMapper.readTree(responseString);

                if (rootNode.has("errors")) {
                    return StatResponse.error("error", "user does not exist");
                } else {
                    return decodeGraphqlJson(rootNode);
                }
            } else {
                return StatResponse.error("error", "Unexpected response: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException ex) {
            return StatResponse.error("error", ex.getResponseBodyAsString());
        } catch (Exception ex) {
            return StatResponse.error("error", ex.getMessage());
        }
    }

    private static HttpEntity<String> getStringHttpEntity(String username) {
        String query = String.format("{\"query\":\"%s\",\"variables\":{\"username\":\"%s\"}}",
                LeetCodeGraphQL.USER_PROFILE_QUERY, username);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("referer", String.format("https://leetcode.com/%s/", username));

        return new HttpEntity<>(query, headers);
    }

    private StatResponse decodeGraphqlJson(JsonNode jsonNode) {
        int totalSolved = 0;
        int totalQuestions = 0;
        int easySolved = 0;
        int totalEasy = 0;
        int mediumSolved = 0;
        int totalMedium = 0;
        int hardSolved = 0;
        int totalHard = 0;
        float acceptanceRate = 0;
        int ranking = 0;
        int contributionPoints = 0;
        int reputation = 0;

        Map<String, Integer> submissionCalendar = new TreeMap<>();

        try {
            JsonNode dataNode = jsonNode.path("data");
            JsonNode allQuestions = dataNode.path("allQuestionsCount");
            JsonNode matchedUserNode = dataNode.path("matchedUser");
            JsonNode submitStats = matchedUserNode.path("submitStats");
            JsonNode actualSubmissions = submitStats.path("acSubmissionNum");
            JsonNode totalSubmissions = submitStats.path("totalSubmissionNum");

            // Fill in total counts
            totalQuestions = allQuestions.get(0).path("count").asInt();
            totalEasy = allQuestions.get(1).path("count").asInt();
            totalMedium = allQuestions.get(2).path("count").asInt();
            totalHard = allQuestions.get(3).path("count").asInt();

            // Fill in solved counts
            totalSolved = actualSubmissions.get(0).path("count").asInt();
            easySolved = actualSubmissions.get(1).path("count").asInt();
            mediumSolved = actualSubmissions.get(2).path("count").asInt();
            hardSolved = actualSubmissions.get(3).path("count").asInt();

            // Fill in acceptance rate
            float totalAcceptCount = actualSubmissions.get(0).path("submissions").floatValue();
            float totalSubCount = totalSubmissions.get(0).path("submissions").floatValue();
            if (totalSubCount != 0) {
                acceptanceRate = round((totalAcceptCount / totalSubCount) * 100);
            }

            // Fill in other details
            contributionPoints = matchedUserNode.path("contributions").path("points").asInt();
            reputation = matchedUserNode.path("profile").path("reputation").asInt();
            ranking = matchedUserNode.path("profile").path("ranking").asInt();

            // Parse submission calendar
            JsonNode submissionCalendarJson = objectMapper.readTree(matchedUserNode.path("submissionCalendar").asText());

            submissionCalendarJson.fieldNames().forEachRemaining(timeKey -> {
                submissionCalendar.put(timeKey, submissionCalendarJson.path(timeKey).asInt());
            });

        } catch (Exception ex) {
            return StatResponse.error("error", ex.getMessage());
        }

        return new StatResponse("success", "retrieved", totalSolved, totalQuestions, easySolved,
                totalEasy, mediumSolved, totalMedium, hardSolved, totalHard, acceptanceRate, ranking,
                contributionPoints, reputation, submissionCalendar);
    }

    private float round(float d) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.floatValue();
    }
}
