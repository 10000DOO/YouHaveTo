package com.yht.exerciseassist.domain.post.repository;

import com.yht.exerciseassist.domain.post.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface PostRepositoryCustom {

    Optional<Post> findByIdWithRole(Long id, String role);

    Slice<Post> postAsSearchType(String role, List<String> postType, List<String> WorkOutCategory, Pageable pageable);
}
