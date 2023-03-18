package com.yht.exerciseassist.domain.comment.repository;

import com.yht.exerciseassist.domain.comment.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CommentRepositoryCustom {

    Slice<Comment> findParentAndChildComment(String role, Long postId, Long parentId, String username, Pageable pageable);
}
