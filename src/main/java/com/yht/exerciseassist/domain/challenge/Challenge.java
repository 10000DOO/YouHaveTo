package com.yht.exerciseassist.domain.challenge;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Challenge {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "challenge_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String challengeContent;

    private String deadline;

    @Embedded
    private DateTime dateTime;
}
