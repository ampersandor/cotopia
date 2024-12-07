package com.ampersandor.cotopia.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String role;
    
    @Column(nullable = false)
    private String password;
    
    private String leetCodeId;
    private String acmicpcId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public void updateRole(String role) {
        this.role = role;
    }

    public void updateProfile(String leetCodeId, String acmicpcId) {
        this.leetCodeId = leetCodeId;
        this.acmicpcId = acmicpcId;
    }
}