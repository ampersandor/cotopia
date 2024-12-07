package com.ampersandor.cotopia.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stat {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "total_solved")
    private int totalSolved;
    
    private LocalDate date;

    public Stat(Long memberId, int totalSolved, LocalDate date) {
        this.memberId = memberId;
        this.totalSolved = totalSolved;
        this.date = date;
    }
}
