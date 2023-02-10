package com.yht.exerciseassist.domain.member.repository;

import com.yht.exerciseassist.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByLoginId(String loginId);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByUsername(String username);

    Optional<Member> findByRefreshToken(String refreshToken);
}
