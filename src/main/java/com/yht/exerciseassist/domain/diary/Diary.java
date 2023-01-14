package com.yht.exerciseassist.domain.diary;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Diary {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="diary_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    private String trainingName;//운동 이름

    private int reps;//횟수

    private int exSetCount;//세트

    private String review;//후기 or 평가

    @Embedded
    private DateTime dateTime;
}
