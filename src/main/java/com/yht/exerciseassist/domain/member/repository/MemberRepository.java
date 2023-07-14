package com.yht.exerciseassist.domain.member.repository;

import com.yht.exerciseassist.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(value = "select m from Member m where m.loginId = :loginId and m.dateTime.canceledAt = null")
    Optional<Member> findByLoginId(@Param("loginId") String loginId);

    @Query(value = "select m from Member m where m.username = :username and m.dateTime.canceledAt = null")
    Optional<Member> findByNotDeletedUsername(@Param("username") String username);

    @Query(value = "select m from Member m where m.refreshToken.refreshToken = :refreshToken and m.dateTime.canceledAt = null")
    Optional<Member> findByRefreshToken(@Param("refreshToken") String refreshToken);

    @Query(value = "select m from Member m where m.email = :email and m.dateTime.canceledAt = null")
    Optional<Member> findByEmail(@Param("email") String email);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "update Member m set m.refreshToken = null where m.refreshToken.id in :TokenId")
    void updateByRefreshToken(@Param("TokenId") List<Long> TokenId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "delete from Member m where m.dateTime.canceledAt < :minusDays")
    void deleteByCancealedAt(@Param("minusDays") String minusDays);
}
