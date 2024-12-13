package com.ampersandor.cotopia.dto;

import com.ampersandor.cotopia.entity.Team;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TeamDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateRequest {
        @NotBlank(message = "Team name cannot be empty")
        private String name;

        public Team toEntity(Long leaderId) {
            return Team.builder()
                    .name(this.name)
                    .leaderId(leaderId)
                    .createdAt(LocalDateTime.now())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest {
        @NotBlank(message = "Team name cannot be empty")
        private String name;

        public Team toEntity() {
            return Team.builder()
                    .name(this.name)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String name;
        private Long leaderId;
        private LocalDateTime createdAt;
        private List<UserDTO.Response> users;
        private List<FoodDTO.Response> foods;

        public static Response from(Team team) {
            return Response.builder()
                    .id(team.getId())
                    .name(team.getName())
                    .leaderId(team.getLeaderId())
                    .createdAt(team.getCreatedAt())
                    .users(team.getUsers().stream().map(UserDTO.Response::from).collect(Collectors.toList()))
                    .foods(team.getFoods().stream().map(FoodDTO.Response::from).collect(Collectors.toList()))
                    .build();
        }
    }

}