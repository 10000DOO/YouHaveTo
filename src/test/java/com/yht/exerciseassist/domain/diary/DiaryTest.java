package com.yht.exerciseassist.domain.diary;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.member.MemberType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

class DiaryTest {

    @Test
    public void createDiary() {
        //given
        Member member = Member.builder()
                .username("username")
                .email("test@test.com")
                .loginId("testId3")
                .dateTime(new DateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), null))
                .role(MemberType.USER)
                .password("testPassword3!")
                .field("서울시")
                .build();
        //when
        Diary diary = Diary.builder()
                .member(member)
                .exerciseName("pushUp")
                .reps(10)
                .exSetCount(10)
                .review("열심히 했다 오운완")
                .cardio(true)
                .cardioTime(30)
                .exerciseDate("2023-01-30")
                .dateTime(new DateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), null))
                .build();
        //then
        assertThat(diary.getMember()).isEqualTo(member);
        assertThat(diary.getExerciseName()).isEqualTo("pushUp");
        assertThat(diary.getReps()).isEqualTo(10);
        assertThat(diary.getExSetCount()).isEqualTo(10);
        assertThat(diary.getReview()).isEqualTo("열심히 했다 오운완");
        assertThat(diary.isCardio()).isTrue();
        assertThat(diary.getCardioTime()).isEqualTo(30);
        assertThat(diary.getExerciseDate()).isEqualTo("2023-01-30");
        assertThat(diary.getDateTime()).isEqualTo(new DateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), null));
    }
}