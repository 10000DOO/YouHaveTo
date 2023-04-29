package com.yht.exerciseassist.domain.diary;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.media.Media;
import com.yht.exerciseassist.domain.member.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
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

    private String exerciseDate;//운동 날짜

    @Size(max = 1000)
    private String review;//후기 or 평가

    @ElementCollection
    private List<ExerciseInfo> exerciseInfo;

    @Embedded
    private DateTime dateTime;

    @OneToMany(mappedBy = "diary")
    private List<Media> mediaList = new ArrayList<>();

    @Builder
    public Diary(Member member, String exerciseDate, String review, List<ExerciseInfo> exerciseInfo, DateTime dateTime) {
        this.member = member;
        this.exerciseDate = exerciseDate;
        this.review = review;
        this.exerciseInfo = exerciseInfo;
        this.dateTime = dateTime;
    }

    public void linkToMedia(List<Media> mediaList) {
        this.mediaList = mediaList;
        for (Media media : mediaList) {
            media.linkToDiary(this);
        }
    }

    public void setDiaryIdUsedOnlyTest(Long id) {
        this.id = id;
    }

    public void editDiary(String exerciseDate, String review, List<ExerciseInfo> exerciseInfo) {
        this.exerciseDate = exerciseDate;
        this.review = review;
        this.exerciseInfo = exerciseInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Diary that = (Diary) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
