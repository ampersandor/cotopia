package com.ampersandor.cotopia.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.ampersandor.cotopia.entity.User;

import lombok.*;


public class UserDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String username;
        private String email;
        private Long teamId;
        private String role;
        private List<CodingAccountDTO.Response> codingAccounts;

        public static Response from(User user) {
            Response dto = new Response();
            dto.id = user.getId();
            dto.username = user.getUsername();
            dto.email = user.getEmail();
            dto.teamId = user.getTeam() != null ? user.getTeam().getId() : null;
            dto.role = user.getRole();
            dto.codingAccounts = user.getCodingAccounts().stream()
                    .map(CodingAccountDTO.Response::from)
                    .collect(Collectors.toList());
            return dto;
        }
    }
}
