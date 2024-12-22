package com.ampersandor.cotopia.handler;

import com.ampersandor.cotopia.event.LikeUpdateEvent;
import com.ampersandor.cotopia.service.LunchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.lang.NonNull;
import java.io.IOException;
import java.util.Set;   
import java.util.concurrent.ConcurrentHashMap;
import com.ampersandor.cotopia.dto.LunchDTO;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.core.JsonProcessingException;

@Slf4j
@Component
@RequiredArgsConstructor
public class LunchWebSocketHandler extends TextWebSocketHandler {
    
    private final LunchService lunchService;
    private final ObjectMapper objectMapper;

    private final Map<Long, Set<WebSocketSession>> teamSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        if (session.getUri() == null) return;
        
        String query = session.getUri().getQuery();
        Long teamId = extractTeamId(query);
        
        teamSessions.computeIfAbsent(teamId, k -> ConcurrentHashMap.newKeySet()).add(session);
    }
    
    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) {
        String payload = message.getPayload();
        log.info("Received message: " + payload);
        
        try {
            LunchDTO.LikeRequest likeRequest = objectMapper.readValue(payload, LunchDTO.LikeRequest.class);
            lunchService.addLikeCount(likeRequest.getLunchId(), likeRequest.getLikeCount());
            
        } catch (IOException e) {
            log.error("Error parsing message: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error handleTextMessage: " + e.getMessage());
        }
    }
    
    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        if (session.getUri() == null) return;
        String query = session.getUri().getQuery();
        Long teamId = extractTeamId(query);
        Set<WebSocketSession> sessions = teamSessions.get(teamId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                teamSessions.remove(teamId);
            }
        }
    }
    
    public void broadcastToTeam(Long teamId, String message) {
        Set<WebSocketSession> sessions = this.teamSessions.get(teamId);
        if (sessions != null) {
            sessions.forEach(session -> {
                try {
                    if (session.isOpen()) {
                        session.sendMessage(new TextMessage(message));
                    }
                } catch (IOException e) {
                    log.error("Failed to send message to session: " + e.getMessage());
                }
            });
        }
    }
    
    @EventListener
    public void handleLikeUpdateEvent(LikeUpdateEvent event) {
        log.info("Handling like update event: " + event.toJson(objectMapper));
        event.getUpdates().forEach((teamId, updateInfo) -> {
            try {
                String message = objectMapper.writeValueAsString(Map.of(
                    "type", "LIKE_UPDATE",
                    "updates", updateInfo.getLunchLikes()
                ));
                broadcastToTeam(teamId, message);
            } catch (JsonProcessingException e) {
                log.error("Failed to serialize like update message", e);
            }
        });
    }

    private Long extractTeamId(String query) {
        if (query != null) {
            String[] params = query.split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2 && "teamId".equals(keyValue[0])) {
                    return Long.parseLong(keyValue[1]);
                }
            }
        }
        throw new IllegalArgumentException("teamId is required");
    }
} 