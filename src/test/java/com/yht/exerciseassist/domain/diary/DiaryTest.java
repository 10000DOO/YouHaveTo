package com.yht.exerciseassist.domain.diary;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.factory.DiaryFactory;
import com.yht.exerciseassist.domain.factory.MemberFactory;
import com.yht.exerciseassist.domain.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
class DiaryTest {

    @Test
    public void createDiary() {
        //given
        Member member = MemberFactory.createTestMember();
        //when
        Diary diary = DiaryFactory.createTestDiary(member);
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
