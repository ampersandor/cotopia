package com.ampersandor.cotopia.dto;

import java.time.LocalDate;

import com.ampersandor.cotopia.entity.Stat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class StatDTO {
    
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String username;
        private LocalDate date;
        private int problemsSolved;
        private String platform;

        public static Response from(Stat stat) {
            return Response.builder()
                    .id(stat.getId())
                    .username(stat.getCodingAccount().getUser().getUsername())
                    .date(stat.getDate())
                    .problemsSolved(stat.getProblemsSolved())
                    .platform(stat.getPlatform())
                    .build();
        }
    }
}
