package com.ampersandor.leettrack.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Stat {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "total_solved") // 컬럼 이름을 member_id로 설정
    private int totalSolved;
    private LocalDate date;

    public Stat(){}

    public Stat(Long memberId, int totalSolved, LocalDate date) {
        this.memberId = memberId;
        this.totalSolved = totalSolved;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public int getTotalSolved() {
        return totalSolved;
    }

    public void setTotalSolved(int totalSolved) {
        this.totalSolved = totalSolved;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Stat{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", totalSolved=" + totalSolved +
                ", date=" + date +
                '}';
    }
}
