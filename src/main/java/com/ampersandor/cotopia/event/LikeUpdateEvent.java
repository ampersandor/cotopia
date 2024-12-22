package com.ampersandor.cotopia.event;

import org.springframework.context.ApplicationEvent;
import lombok.Getter;
import lombok.AllArgsConstructor;
import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Getter
public class LikeUpdateEvent extends ApplicationEvent {
    private final Map<Long, LikeUpdateInfo> updates;

    public LikeUpdateEvent(Object source, Map<Long, LikeUpdateInfo> updates) {
        super(source);
        this.updates = updates;
    }

    public String toJson(ObjectMapper objectMapper) {
        try {
            return objectMapper.writeValueAsString(updates);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize updates to JSON", e);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class LikeUpdateInfo {
        private final Map<Long, Integer> lunchLikes;
    }
} 