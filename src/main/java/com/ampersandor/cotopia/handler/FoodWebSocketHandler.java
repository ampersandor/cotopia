package com.ampersandor.cotopia.handler;

import com.ampersandor.cotopia.event.LikeUpdateEvent;
import com.ampersandor.cotopia.service.FoodService;
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
import org.springframework.beans.factory.annotation.Autowired;
import com.ampersandor.cotopia.dto.FoodDTO;

@Component
public class FoodWebSocketHandler extends TextWebSocketHandler {
    
    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
    private final FoodService foodService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public FoodWebSocketHandler(FoodService foodService) {
        this.foodService = foodService;
    }
    
    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        sessions.add(session);
        System.out.println("WebSocket connected: " + session.getId());
    }
    
    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) {
        String payload = message.getPayload();
        System.out.println("Received message: " + payload);
        
        try {
            // JSON 파싱
            LikeRequest likeRequest = objectMapper.readValue(payload, LikeRequest.class);
            
            // 좋아요 수 증가
            foodService.addLikeCount(likeRequest.getFoodId(), likeRequest.getLikeCount());
            
        } catch (IOException e) {
            System.err.println("Error parsing message: " + e.getMessage());
        }
    }
    
    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        sessions.remove(session);
        System.out.println("WebSocket closed: " + session.getId());
    }
    
    public void broadcastMessage(String message) {
        TextMessage textMessage = new TextMessage(message);
        sessions.forEach(session -> {
            try {
                session.sendMessage(textMessage);
            } catch (IOException e) {
                System.err.println("Error sending message: " + e.getMessage());
            }
        });
    }
    
    // 내부 클래스 또는 별도의 파일로 정의할 수 있습니다.
    public static class LikeRequest {
        private Long foodId;
        private Long teamId;
        private int likeCount;

        // Getters and Setters
        public Long getFoodId() {
            return foodId;
        }

        public void setFoodId(Long foodId) {
            this.foodId = foodId;
        }

        public Long getTeamId() {
            return teamId;
        }

        public void setTeamId(Long teamId) {
            this.teamId = teamId;
        }

        public int getLikeCount() {
            return this.likeCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }
    }

    @EventListener
    public void handleLikeUpdateEvent(LikeUpdateEvent event) {
        try {
            FoodDTO.LikeResponse response = FoodDTO.LikeResponse.builder()
                .foodId(event.getFoodId())
                .likeCount(event.getLikeCount())
                .build();
            
            String jsonResponse = objectMapper.writeValueAsString(response);
            broadcastMessage(jsonResponse);
        } catch (IOException e) {
            System.err.println("Error converting message to JSON: " + e.getMessage());
        }
    }
} 