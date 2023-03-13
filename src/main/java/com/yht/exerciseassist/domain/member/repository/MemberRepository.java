package com.yht.exerciseassist.domain.member.repository;

import com.yht.exerciseassist.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(value = "select m from Member m where m.loginId = :loginId and m.dateTime.canceledAt = null")
    Optional<Member> findByLoginId(String loginId);

    @Query(value = "select m from Member m where m.username = :username and m.dateTime.canceledAt = null")
    Optional<Member> findByUsername(String username);

    @Query(value = "select m from Member m where m.refreshToken.refreshToken = :refreshToken and m.dateTime.canceledAt = null")
    Optional<Member> findByRefreshToken(String refreshToken);

    @Query(value = "select m from Member m where m.email = :email and m.dateTime.canceledAt = null")
    Optional<Member> findByEmail(String email);
}
