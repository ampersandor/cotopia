package com.ampersandor.cotopia.entity;


import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    public static final String DEFAULT_ROLE = "ROLE_USER";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Builder.Default
    private String role = DEFAULT_ROLE;
    
    @Column(nullable = false)
    private String password;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<CodingAccount> codingAccounts = new ArrayList<>();
}
