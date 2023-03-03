package com.yht.exerciseassist.domain.post.repository;

import com.yht.exerciseassist.domain.post.Post;

import java.util.Optional;

public interface PostRepositoryCustom {

    Optional<Post> findNotDeletedById(Long id, String role);
}
