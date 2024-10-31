package com.ampersandor.leettrack.model;

import java.time.LocalDate;

public class Stat {
    private Long id;
    private Long memberId;
    private int totalSolved;
    private LocalDate date;

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
