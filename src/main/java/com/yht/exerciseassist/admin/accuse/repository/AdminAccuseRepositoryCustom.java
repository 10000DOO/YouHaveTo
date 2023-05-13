package com.yht.exerciseassist.admin.accuse.repository;

import com.yht.exerciseassist.domain.accuse.Accuse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface AdminAccuseRepositoryCustom {
    Slice<Accuse> accuseAsAccuseType (List<String> accuseType, List<String> types, Pageable pageable);
}
