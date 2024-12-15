package com.ampersandor.cotopia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import com.ampersandor.cotopia.entity.Food;
import com.ampersandor.cotopia.entity.Team;
import java.time.LocalDate;

public class FoodDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private Long teamId;
        private String name;
        private int likeCount;
        public static Response from(Food food) {
            return Response.builder()
                    .id(food.getId())
                    .teamId(food.getTeam().getId())
                    .name(food.getName())
                    .likeCount(food.getLikeCount())
                    .build();
        }

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class LikeResponse {
        private Long foodId;
        private int likeCount;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private Long teamId;
        private String name;
        private int likeCount;
        private LocalDate date;

        public Food toEntity() {
            return Food.builder()
                    .team(Team.builder().id(teamId).build())
                    .name(name)
                    .likeCount(likeCount)
                    .date(date)
                    .build();
        }
    }


}