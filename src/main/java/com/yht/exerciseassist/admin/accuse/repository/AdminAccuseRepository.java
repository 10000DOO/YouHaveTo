package com.yht.exerciseassist.admin.accuse.repository;

import com.yht.exerciseassist.domain.accuse.Accuse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AdminAccuseRepository extends JpaRepository<Accuse, Long>, AdminAccuseRepositoryCustom {

    @Query("select a from Accuse a where a.dateTime.canceledAt = null and a.id = :accuseId")
    Optional<Accuse> findByNotDeletedId(Long accuseId);
}
