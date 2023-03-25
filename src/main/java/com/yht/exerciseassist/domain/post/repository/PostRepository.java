package com.yht.exerciseassist.domain.post.repository;

import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    @Query(value = "select p from Post p where p.id = :postId and p.dateTime.canceledAt = null")
    Optional<Post> findNotDeletedById(@Param("postId") Long postId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "UPDATE Post p SET p.dateTime.canceledAt = :canceledAt WHERE p.id = :postId and p.dateTime.canceledAt = null")
    void deletePostById(String canceledAt, Long postId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "delete from Post p where p.dateTime.canceledAt < :minusDays")
    void deleteByCancealedAt(@Param("minusDays") String minusDays);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "update Post p set p.postWriter = null where p.postWriter = :member")
    void updatePostWriterToNull(@Param("member") Member member);
}

