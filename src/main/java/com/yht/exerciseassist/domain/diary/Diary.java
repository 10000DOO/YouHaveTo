package com.yht.exerciseassist.domain.diary;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

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

    private String exerciseName;//운동 이름

    private int reps;//횟수

    private int exSetCount;//세트

    private String review;//후기 or 평가

    private boolean cardio; //유산소 = ture 무산소 = false

    private int cardioTime; //유산소 운동 시간

    private String exerciseDate; //운동 날짜

    @Embedded
    private DateTime dateTime;

    @Builder
    public Diary(Member member, String exerciseName, int reps, int exSetCount, String review,
                 boolean cardio, int cardioTime, String exerciseDate, DateTime dateTime) {
        this.member = member;
        this.exerciseName = exerciseName;
        this.reps = reps;
        this.exSetCount = exSetCount;
        this.review = review;
        this.cardio = cardio;
        this.cardioTime = cardioTime;
        this.exerciseDate = exerciseDate;
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Diary diary)) return false;
        return getReps() == diary.getReps() && getExSetCount() == diary.getExSetCount() && isCardio() == diary.isCardio() && getCardioTime() == diary.getCardioTime() && getId().equals(diary.getId()) && getMember().equals(diary.getMember()) && Objects.equals(getExerciseName(), diary.getExerciseName()) && Objects.equals(getReview(), diary.getReview()) && Objects.equals(getExerciseDate(), diary.getExerciseDate()) && getDateTime().equals(diary.getDateTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getMember(), getExerciseName(), getReps(), getExSetCount(), getReview(), isCardio(), getCardioTime(), getExerciseDate(), getDateTime());
    }
}
