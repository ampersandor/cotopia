package com.ampersandor.cotopia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.ampersandor.cotopia.entity.Food;
public class FoodDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private Long teamId;
        private String name;
        private int likes;

        public static Response from(Food food) {
            return Response.builder()
                    .id(food.getId())
                    .teamId(food.getTeam().getId())
                    .name(food.getName())
                    .likes(food.getLikes())
                    .build();
        }
    }
}
