package com.yht.exerciseassist.domain.media.repository;

import com.yht.exerciseassist.domain.media.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media, Long> {

    @Query(value = "select m from Media m join fetch m.diary where m.diary.id = :diaryId and m.dateTime.canceledAt = null")
    List<Media> findByNotDeletedDiaryId(@Param("diaryId") Long diaryId);

    @Query(value = "select m from Media m join fetch m.post where m.post.id = :postId and m.dateTime.canceledAt = null")
    List<Media> findByNotDeletedPostId(@Param("postId") Long postId);

    @Query(value = "select m from Media m where m.id = :mediaId and m.dateTime.canceledAt = null")
    Optional<Media> findByNotDeletedId(@Param("mediaId") Long mediaId);
}
