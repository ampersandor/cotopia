package com.ampersandor.cotopia.util;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

@Component("acmicpc")
public class AcmicpcFetcher implements CodingPlatformFetcher {
    
    private static final String SOLVED_AC_API_URL = "https://solved.ac/api/v3/search/user?query=";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AcmicpcFetcher() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public StatResponse fetchStat(String username) {
        try {
            String response = restTemplate.getForObject(SOLVED_AC_API_URL + username, String.class);
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode userNode = rootNode.path("items").get(0);

            Map<String, Integer> additionalStats = new HashMap<>();
            additionalStats.put("tier", userNode.path("tier").asInt());
            additionalStats.put("rating", userNode.path("rating").asInt());
            additionalStats.put("class", userNode.path("class").asInt());
            additionalStats.put("maxStreak", userNode.path("maxStreak").asInt());

            return new StatResponse(
                "200",                                    // status
                username,                                 // username
                userNode.path("solvedCount").asInt(),    // problemsSolved
                0,                                        // totalSubmissions (API에서 제공하지 않음)
                userNode.path("rating").asInt(),         // rating
                userNode.path("tier").asInt(),           // rank
                userNode.path("class").asInt(),          // level
                0,                                        // easySolved
                0,                                        // mediumSolved
                0,                                        // hardSolved
                userNode.path("maxStreak").asInt(),      // streak
                0,                                        // ranking
                0,                                        // contestRating
                0,                                        // contestRanking
                additionalStats                          // additionalStats
            );

        } catch (Exception e) {
            return new StatResponse(
                "404",
                username,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                new HashMap<>()
            );
        }
    }

    @Override
    public String getPlatform() {
        return "acmicpc";
    }
}
