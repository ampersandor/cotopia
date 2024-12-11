package com.ampersandor.cotopia.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.ampersandor.cotopia.entity.User;

public class ProfileDTO {
    private Long id;
    private String username;
    private String email;
    private Long teamId;
    private String role;
    private List<CodingAccountDTO> codingAccounts;

    public ProfileDTO() {
    }

    public ProfileDTO(Long id, String username, String email, Long teamId, String role, List<CodingAccountDTO> codingAccounts) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.teamId = teamId;
        this.role = role;
        this.codingAccounts = codingAccounts;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public Long getTeamId() { return teamId; }
    public String getRole() { return role; }
    public List<CodingAccountDTO> getCodingAccounts() { return codingAccounts; }

    public static ProfileDTO from(User user) {
        List<CodingAccountDTO> accountDTOs = user.getCodingAccounts().stream()
                .map(CodingAccountDTO::from)
                .collect(Collectors.toList());

        return new ProfileDTO(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getTeam() != null ? user.getTeam().getId() : null,
            user.getRole(),
            accountDTOs
        );
    }
} 