package com.yht.exerciseassist.domain.diary.repository;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.diary.Diary;
import com.yht.exerciseassist.domain.diary.ExerciseInfo;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.member.MemberType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class DiaryRepositoryTest {

    @Autowired
    private DiaryRepository diaryRepository;

    @Autowired
    private EntityManager em;

    @Test
    public void saveDiary() {
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

        em.persist(member);

        em.flush();
        em.clear();

        Member findMember = em.find(Member.class, member.getId());

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
                .member(findMember)
                .exerciseInfo(exInfoList)
                .review("열심히 했다 오운완")
                .exerciseDate("2023-01-30")
                .dateTime(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null))
                .build();
        //when
        Diary savedDiary = diaryRepository.save(diary);
        //then
        assertThat(savedDiary).isEqualTo(diary);
    }

    @Test
    public void findDiariesByUsername() {
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

        em.persist(member);

        em.flush();
        em.clear();

        Member findMember = em.find(Member.class, member.getId());

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
                .member(findMember)
                .exerciseInfo(exInfoList)
                .review("열심히 했다 오운완")
                .exerciseDate("2023-01-30")
                .dateTime(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null))
                .build();

        diaryRepository.save(diary);
        em.flush();
        em.clear();
        //when
        List<Diary> diariesByUsername = diaryRepository.findDiariesByUsername(findMember.getUsername(), "2023-01");
        //then
        assertThat(diariesByUsername.get(0)).isEqualTo(diary);
    }

    @Test
    public void findDiaryDetailsByUsername() {
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

        em.persist(member);

        em.flush();
        em.clear();

        Member findMember = em.find(Member.class, member.getId());

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
                .member(findMember)
                .exerciseInfo(exInfoList)
                .review("열심히 했다 오운완")
                .exerciseDate("2023-02-23")
                .dateTime(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null))
                .build();

        diaryRepository.save(diary);
        em.flush();
        em.clear();
        //when
        Optional<Diary> diaryDetailsByUsername = diaryRepository.findDiaryDetailsByUsername(findMember.getUsername(), "2023-02-23");
        //then
        assertThat(diaryDetailsByUsername.get()).isEqualTo(diary);
    }
}
