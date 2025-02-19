package com.ampersandor.cotopia.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Team {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "leader_id", nullable = false)
    private Long leaderId;

    @OneToMany(mappedBy = "team")
    @Builder.Default
    private List<User> users = new ArrayList<>();
    
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Lunch> lunches = new ArrayList<>();

    public void addLunch(Lunch lunch) {
        lunches.add(lunch);
        lunch.setTeam(this);
    }

    public void addLunches(List<Lunch> lunches) {
        lunches.forEach(this::addLunch);
    }
}
