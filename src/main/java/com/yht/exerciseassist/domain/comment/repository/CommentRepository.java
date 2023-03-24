package com.yht.exerciseassist.domain.comment.repository;

import com.yht.exerciseassist.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    @Query(value = "select c from Comment c where c.id = :parentId and c.dateTime.canceledAt = null")
    Optional<Comment> findParentCommentByParentId(@Param("parentId") Long parentId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Comment c SET c.dateTime.canceledAt = :canceledAt WHERE c.parent.id = :parentId and c.dateTime.canceledAt = null")
    void deleteCommentByParentId(String canceledAt, Long parentId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Comment c SET c.dateTime.canceledAt = :canceledAt WHERE c.post.id = :postId and c.dateTime.canceledAt = null")
    void deleteCommentByPostId(String canceledAt, Long postId);

    @Modifying(clearAutomatically = true)
    @Query(value = "delete from Comment c where c.dateTime.canceledAt < :minusDays")
    void deleteByCancealedAt(@Param("minusDays") String minusDays);
}
