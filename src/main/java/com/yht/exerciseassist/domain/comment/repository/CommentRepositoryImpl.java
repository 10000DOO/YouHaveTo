package com.yht.exerciseassist.domain.comment.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yht.exerciseassist.domain.comment.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.yht.exerciseassist.domain.comment.QComment.comment;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public Slice<Comment> findParentAndChildComment(String role, Long postId, Long parentId, String username, Pageable pageable) {
        List<Comment> comments = queryFactory
                .selectFrom(comment)
                .where(memberRoleEq(role), postIdEq(postId), parentIdEq(parentId, username), usernameEq(username))
                .orderBy(comment.dateTime.createdAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = comments.size() > pageable.getPageSize();
        return new SliceImpl<>(comments, pageable, hasNext);
    }

    private BooleanExpression memberRoleEq(String role) {
        return role.equals("USER") ? comment.dateTime.canceledAt.isNull() : null;
    }

    private BooleanExpression postIdEq(Long postId) {
        return postId == null ? null : comment.post.id.eq(postId);
    }

    private BooleanExpression parentIdEq(Long parentId, String username) {
        if (parentId == null && username != null)
            return null;
        else if (parentId == null) {
            return comment.parent.id.isNull();
        } else
            return comment.parent.id.eq(parentId);
    }

    private BooleanExpression usernameEq(String username) {
        return username == null ? null : comment.commentWriter.username.eq(username);
    }

}
