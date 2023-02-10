package com.yht.exerciseassist.domain.diary.repository;

import com.yht.exerciseassist.domain.diary.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

//    @Query(value = "select d from Diary d join fetch d.member where d.member.username = :username and DATE_FORMAT(d.exerciseDate, '%Y-%m') = :date")
//    List<Diary> findDiariesByUsername(String username, String date);
}
