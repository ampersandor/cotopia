package com.ampersandor.cotopia.dto;

import com.ampersandor.cotopia.entity.Team;
import com.ampersandor.cotopia.entity.User;
import com.ampersandor.cotopia.entity.Food;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TeamDTO {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private Long leaderId;
    private List<Long> userIds;
    private List<Long> foodIds;

    // 기본 생성자
    public TeamDTO() {
    }

    // 모든 필드를 포함하는 생성자
    public TeamDTO(Long id, String name, LocalDateTime createdAt, Long leaderId, 
                  List<Long> userIds, List<Long> foodIds) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.leaderId = leaderId;
        this.userIds = userIds;
        this.foodIds = foodIds;
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Long getLeaderId() { return leaderId; }
    public List<Long> getUserIds() { return userIds; }
    public List<Long> getFoodIds() { return foodIds; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setLeaderId(Long leaderId) { this.leaderId = leaderId; }
    public void setUserIds(List<Long> userIds) { this.userIds = userIds; }
    public void setFoodIds(List<Long> foodIds) { this.foodIds = foodIds; }

    // Entity를 DTO로 변환하는 정적 메서드
    public static TeamDTO from(Team team) {
        List<Long> userIds = team.getUsers().stream()
                .map(User::getId)
                .collect(Collectors.toList());
        
        List<Long> foodIds = team.getFoods().stream()
                .map(Food::getId)
                .collect(Collectors.toList());

        return new TeamDTO(
            team.getId(),
            team.getName(),
            team.getCreatedAt(),
            team.getLeaderId(),
            userIds,
            foodIds
        );
    }
}