package com.yht.exerciseassist.domain.comment.repository;

import com.yht.exerciseassist.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "select c from Comment c where c.id = :parentId and c.dateTime.canceledAt = null")
    Optional<Comment> findParentCommentByParentId(@Param("parentId") Long parentId);
}
