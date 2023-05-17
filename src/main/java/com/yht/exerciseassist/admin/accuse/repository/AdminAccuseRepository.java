package com.yht.exerciseassist.admin.accuse.repository;

import com.yht.exerciseassist.domain.accuse.Accuse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminAccuseRepository extends JpaRepository<Accuse, Long>, AdminAccuseRepositoryCustom {

}
