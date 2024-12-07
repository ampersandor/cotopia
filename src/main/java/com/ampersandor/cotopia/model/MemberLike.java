package com.ampersandor.cotopia.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member_likes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberLike {
    @Id
    @Column(name = "member_id")
    private Long memberId;

    @Column(nullable = false)
    private Long count;
}