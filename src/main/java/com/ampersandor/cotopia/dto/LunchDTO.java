package com.ampersandor.cotopia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import com.ampersandor.cotopia.entity.Lunch;
import com.ampersandor.cotopia.entity.Team;
import java.time.LocalDate;

public class LunchDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private Long teamId;
        private String name;
        private int likeCount;
        public static Response from(Lunch lunch) {
            return Response.builder()
                    .id(lunch.getId())
                    .teamId(lunch.getTeam().getId())
                    .name(lunch.getName())
                    .likeCount(lunch.getLikeCount())
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
        private String name;
        private int likeCount;
        private LocalDate date;

        public Lunch toEntity() {
            return Lunch.builder()
                    .team(Team.builder().id(teamId).build())
                    .name(name)
                    .likeCount(likeCount)
                    .date(date)
                    .build();
        }
    }


}