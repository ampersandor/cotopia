package com.ampersandor.cotopia.dto;

import java.time.LocalDate;

import com.ampersandor.cotopia.entity.Stat;

public class StatDTO {
    private Long id;
    private Long codingAccountId;
    private LocalDate date;
    private int problemsSolved;
    private String platform;
    private String username;


    // 기본 생성자
    public StatDTO() {
    }

    // 모든 필드를 포함하는 생성자
    public StatDTO(Long id, Long codingAccountId, LocalDate date, int problemsSolved, String platform, String username) {
        this.id = id;
        this.codingAccountId = codingAccountId;
        this.date = date;
        this.problemsSolved = problemsSolved;
        this.platform = platform;
        this.username = username;
    }

    // Getters
    public Long getId() { return id; }
    public Long getCodingAccountId() { return codingAccountId; }
    public LocalDate getDate() { return date; }
    public int getProblemsSolved() { return problemsSolved; }
    public String getPlatform() { return platform; }
    public String getUsername() { return username; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setCodingAccountId(Long codingAccountId) { this.codingAccountId = codingAccountId; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setProblemsSolved(int problemsSolved) { this.problemsSolved = problemsSolved; }
    public void setPlatform(String platform) { this.platform = platform; }
    public void setUsername(String username) { this.username = username; }

    // Entity를 DTO로 변환하는 정적 메서드
    public static StatDTO from(Stat stat) {
        return new StatDTO(
            stat.getId(),
            stat.getCodingAccount().getId(),
            stat.getDate(),
            stat.getProblemsSolved(),
            stat.getPlatform(),
            stat.getCodingAccount().getUser().getUsername()
        );
    }
}
