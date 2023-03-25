package com.yht.exerciseassist.domain.refreshToken.repository;

import com.yht.exerciseassist.domain.refreshToken.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Query(value = "select r.id from RefreshToken r where r.dateTime.updatedAt < :minusDays")
    List<Long> findByUpdatedAtBefore(@Param("minusDays") String minusDays);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "delete from RefreshToken r where r.dateTime.updatedAt < :minusDays")
    void deleteByUpdatedAtBefore(@Param("minusDays") String minusDays);
}
