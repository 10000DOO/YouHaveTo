package com.yht.exerciseassist.domain.post.repository;

import com.yht.exerciseassist.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "select p from Post p where p.dateTime.canceledAt = null and p.id = :id")
    Optional<Post> findNotDeletedById(Long id);
}
