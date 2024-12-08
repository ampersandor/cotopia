package com.ampersandor.cotopia.dto;

import java.time.LocalDate;

import com.ampersandor.cotopia.entity.Stat;

public class StatDTO {
    private Long id;
    private Long codingAccountId;
    private Long userId;
    private LocalDate date;
    private int problemsSolved;
    private String platform;

    // 기본 생성자
    public StatDTO() {
    }

    // 모든 필드를 포함하는 생성자
    public StatDTO(Long id, Long codingAccountId, Long userId, LocalDate date, int problemsSolved, String platform) {
        this.id = id;
        this.codingAccountId = codingAccountId;
        this.userId = userId;
        this.date = date;
        this.problemsSolved = problemsSolved;
        this.platform = platform;
    }

    // Getters
    public Long getId() { return id; }
    public Long getCodingAccountId() { return codingAccountId; }
    public Long getUserId() { return userId; }
    public LocalDate getDate() { return date; }
    public int getProblemsSolved() { return problemsSolved; }
    public String getPlatform() { return platform; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setCodingAccountId(Long codingAccountId) { this.codingAccountId = codingAccountId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setProblemsSolved(int problemsSolved) { this.problemsSolved = problemsSolved; }
    public void setPlatform(String platform) { this.platform = platform; }

    // Entity를 DTO로 변환하는 정적 메서드
    public static StatDTO from(Stat stat) {
        return new StatDTO(
            stat.getId(),
            stat.getCodingAccount().getId(),
            stat.getUser().getId(),
            stat.getDate(),
            stat.getProblemsSolved(),
            stat.getPlatform()
        );
    }
}
