package com.ampersandor.cotopia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import com.ampersandor.cotopia.handler.FoodWebSocketHandler;
import com.ampersandor.cotopia.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    
    private final FoodService foodService;

    @Autowired
    public WebSocketConfig(FoodService foodService) {
        this.foodService = foodService;
    }

    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(foodService), "/ws")
               .setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler webSocketHandler(FoodService foodService) {
        return new FoodWebSocketHandler(foodService);
    }
}
