package com.yht.exerciseassist.domain.diary;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.member.MemberType;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
class DiaryTest {

    @Test
    public void createDiary() {
        //given
        Member member = Member.builder()
                .username("username")
                .email("test@test.com")
                .loginId("testId3")
                .dateTime(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null))
                .role(MemberType.USER)
                .password("testPassword3!")
                .field("서울시")
                .build();

        ExerciseInfo exInfo = ExerciseInfo.builder()
                .exerciseName("pushUp")
                .reps(10)
                .exSetCount(10)
                .cardio(true)
                .cardioTime(30)
                .finished(true)
                .build();

        List<ExerciseInfo> exInfoList = new ArrayList<>();
        exInfoList.add(exInfo);


        //when
        Diary diary = Diary.builder()
                .member(member)
                .exerciseInfo(exInfoList)
                .review("열심히 했다 오운완")
                .exerciseDate("2023-01-30")
                .dateTime(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null))
                .build();

        //then
        assertThat(diary.getMember()).isEqualTo(member);
        assertThat(diary.getExerciseInfo().get(0).getExerciseName()).isEqualTo("pushUp");
        assertThat(diary.getExerciseInfo().get(0).getReps()).isEqualTo(10);
        assertThat(diary.getExerciseInfo().get(0).getExSetCount()).isEqualTo(10);
        assertThat(diary.getReview()).isEqualTo("열심히 했다 오운완");
        assertThat(diary.getExerciseInfo().get(0).isCardio()).isTrue();
        assertThat(diary.getExerciseInfo().get(0).getCardioTime()).isEqualTo(30);
        assertThat(diary.getExerciseDate()).isEqualTo("2023-01-30");
        assertThat(diary.getDateTime()).isEqualTo(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null));
    }
}
