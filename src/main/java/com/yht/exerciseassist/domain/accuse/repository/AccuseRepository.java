package com.yht.exerciseassist.domain.accuse.repository;

import com.yht.exerciseassist.domain.accuse.Accuse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccuseRepository extends JpaRepository<Accuse, Long> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "delete from Accuse a where a.dateTime.canceledAt < :minusDays")
    void deleteByCreatedBefore(@Param("minusDays") String minusDays);
}
