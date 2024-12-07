package com.ampersandor.cotopia.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String username;

    public Member(String name, String username) {
        this.name = name;
        this.username = username;
    }
}
