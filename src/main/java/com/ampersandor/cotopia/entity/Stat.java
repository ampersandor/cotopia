package com.ampersandor.cotopia.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Stat {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    private LocalDate date;
    
    @Column(name = "problems_solved")
    private int problemsSolved;
    
    private String source;

    public void setProblemsSolved(int problemsSolved, String source) {
        this.problemsSolved = problemsSolved;
        this.source = source;
    }
}
