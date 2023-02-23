package com.yht.exerciseassist.domain.media.repository;

import com.yht.exerciseassist.domain.media.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaRepository extends JpaRepository<Media, Long> {

    List<Media> findByDiaryId(Long diaryId);
}
