package com.yht.exerciseassist.domain.comment.repository;

import com.yht.exerciseassist.domain.comment.Comment;
import com.yht.exerciseassist.domain.member.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    @Query(value = "select c from Comment c where c.id = :parentId and c.dateTime.canceledAt = null")
    Optional<Comment> findParentCommentByParentId(@Param("parentId") Long parentId);

    @Query(value = "select c from Comment c where c.id = :commentId and c.dateTime.canceledAt = null")
    Optional<Comment> findByNotDeleteId(@Param("commentId") Long commentId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "UPDATE Comment c SET c.dateTime.canceledAt = :canceledAt, c.post = null, c.parent = null WHERE c.id = :commentId")
    void deleteCommentByParentId(@Param("canceledAt") String canceledAt, @Param("commentId") Long commentId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "UPDATE Comment c SET c.dateTime.canceledAt = :canceledAt, c.post = null WHERE c.id = :commentId")
    void deleteCommentBycommentId(@Param("canceledAt") String canceledAt, @Param("commentId") Long commentId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "UPDATE Comment c SET c.dateTime.canceledAt = :canceledAt WHERE c.post.id = :postId and c.dateTime.canceledAt = null")
    void deleteCommentByPostId(@Param("canceledAt") String canceledAt, @Param("postId") Long postId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "delete from Comment c where c.dateTime.canceledAt < :minusDays")
    void deleteByCancealedAt(@Param("minusDays") String minusDays);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "update Comment c set c.commentWriter = null where c.commentWriter = :member and c.dateTime.canceledAt = null")
    void updateCommentWriterToNull(@Param("member") Member member);

    @Query(value = "select c from Comment c where c.post.id = :postId and c.parent = null")
    Slice<Comment> findParentComment(@Param("postId") Long postId, Pageable pageable);
}
