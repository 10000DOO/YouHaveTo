package com.yht.exerciseassist.domain.diary.repository;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.diary.Diary;
import com.yht.exerciseassist.domain.diary.ExerciseInfo;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.member.MemberType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DiaryRepositoryTest {

    @Autowired
    private DiaryRepository diaryRepository;

    @Test
    public void saveDiary() {
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

        Diary diary = Diary.builder()
                .member(member)
                .exerciseInfo(exInfoList)
                .review("열심히 했다 오운완")
                .exerciseDate("2023-01-30")
                .dateTime(new DateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), null))
                .build();
        //when
        Diary savedDiary = diaryRepository.save(diary);
        //then
        assertThat(savedDiary).isEqualTo(diary);
    }
}
