package com.ampersandor.leettrack.model;

import jakarta.persistence.*;

@Entity
@Table(name = "member_likes")
public class MemberLike {
    @Id
    @Column(name = "member_id")
    private Long memberId;

    @Column(nullable = false)
    private Long count;

    // Constructors
    public MemberLike() {
    }

    public MemberLike(Long memberId, Long count) {
        this.memberId = memberId;
        this.count = count;
    }

    // Getters and Setters
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}