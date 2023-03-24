package com.yht.exerciseassist.domain.likeCount.repository;

import com.yht.exerciseassist.domain.likeCount.LikeCount;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeCountRepository extends JpaRepository<LikeCount, Long> {

    @Query("select l from LikeCount l where l.post = :post and l.member = :member")
    Optional<LikeCount> findNotDeletedByPostAndMember(Post post, Member member);

    @Modifying(clearAutomatically = true)
    @Query("delete from LikeCount l where l.post = :post")
    void deleteAllByPost(Post post);

    @Modifying(clearAutomatically = true)
    @Query("delete from LikeCount l where l.member = :member")
    void deleteAllByMember(Member member);
}
