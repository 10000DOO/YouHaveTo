package com.yht.exerciseassist.domain.likeCount.repository;

import com.yht.exerciseassist.domain.likeCount.LikeCount;
import com.yht.exerciseassist.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeCountRepository extends JpaRepository<LikeCount, Long> {

    Optional<LikeCount> findByPost(Post post);

    void deleteByPost(Post post);
}
