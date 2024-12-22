package com.ampersandor.cotopia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import com.ampersandor.cotopia.entity.Lunch;
import com.ampersandor.cotopia.entity.Team;
import com.ampersandor.cotopia.entity.Food;
import java.time.LocalDateTime;

public class LunchDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private Long teamId;
        private Long foodId;
        private String foodName;
        private String foodImgSrc;
        private int likeCount;
        private LocalDateTime createdAt;

        public static Response from(Lunch lunch) {
            return Response.builder()
                    .id(lunch.getId())
                    .teamId(lunch.getTeam().getId())
                    .foodId(lunch.getFood().getId())
                    .foodName(lunch.getFood().getName())
                    .foodImgSrc(lunch.getFood().getImgSrc())
                    .likeCount(lunch.getLikeCount())
                    .createdAt(lunch.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class LikeResponse {
        private Long lunchId;
        private int likeCount;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LikeRequest {
        private Long lunchId;
        private Long teamId;
        private int likeCount;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private Long teamId;
        private Long foodId;
        private int likeCount;

        public Lunch toEntity() {
            return Lunch.builder()
                    .team(Team.builder().id(teamId).build())
                    .food(Food.builder().id(foodId).build())
                    .likeCount(likeCount)
                    .createdAt(LocalDateTime.now())
                    .build();
        }
    }
}