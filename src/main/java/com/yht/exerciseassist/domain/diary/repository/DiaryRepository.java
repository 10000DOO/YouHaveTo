package com.yht.exerciseassist.domain.diary.repository;

import com.yht.exerciseassist.domain.diary.Diary;
import com.yht.exerciseassist.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    @Query(value = "select d from Diary d join fetch d.member where d.member.username = :username and to_char(to_timestamp(d.exerciseDate, 'YYYY-MM'),'YYYY-MM') = :date and d.dateTime.canceledAt = null order by d.exerciseDate")
    List<Diary> findDiariesByUsername(@Param("username") String username, @Param("date") String date);

    @Query(value = "select d from Diary d join fetch d.member where d.member.username = :username and to_char(to_timestamp(d.exerciseDate, 'YYYY-MM-DD'),'YYYY-MM-DD') = :date and d.dateTime.canceledAt = null")
    Optional<Diary> findDiaryDetailsByUsername(@Param("username") String username, @Param("date") String date);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "delete from Diary d where d.dateTime.canceledAt < :minusDays")
    void deleteByCancealedAt(@Param("minusDays") String minusDays);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "UPDATE Diary d SET d.dateTime.canceledAt = :canceledAt WHERE d.member = :member")
    void deleteByMemberId(@Param("canceledAt") String canceledAt, @Param("member") Member member);

}
