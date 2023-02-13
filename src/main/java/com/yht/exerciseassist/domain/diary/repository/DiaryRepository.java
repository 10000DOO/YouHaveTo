package com.yht.exerciseassist.domain.diary.repository;

import com.yht.exerciseassist.domain.diary.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    @Query(value = "select d from Diary d join fetch d.member where d.member.username = :username and to_char(to_timestamp(d.exerciseDate, 'YYYY-MM'),'YYYY-MM') = :date")
    List<Diary> findDiariesByUsername(String username, String date);
}
