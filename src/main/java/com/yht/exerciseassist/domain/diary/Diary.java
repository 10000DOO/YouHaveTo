package com.yht.exerciseassist.domain.diary;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String exerciseDate; //운동 날짜

    private String review;//후기 or 평가

    @ElementCollection
    private List<ExerciseInfo> exerciseInfo;

    @Embedded
    private DateTime dateTime;

    @Builder
    public Diary(Member member, String exerciseDate, String review, List<ExerciseInfo> exerciseInfo, DateTime dateTime) {
        this.member = member;
        this.exerciseDate = exerciseDate;
        this.review = review;
        this.exerciseInfo = exerciseInfo;
        this.dateTime = dateTime;
    }
}
