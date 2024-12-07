package com.ampersandor.cotopia.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Team {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "leader_id", nullable = false)
    private Long leaderId;
    
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    @Builder.Default
    private List<User> users = new ArrayList<>();
    
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Food> foods = new ArrayList<>();

    public void addUser(User user) {
        users.add(user);
        user.setTeam(this);
    }

    public void removeUser(User user) {
        users.remove(user);
        user.setTeam(null);
    }

    public void addFood(Food food) {
        foods.add(food);
        food.setTeam(this);
    }

    public void removeFood(Food food) {
        foods.remove(food);
        food.setTeam(null);
    }
}
