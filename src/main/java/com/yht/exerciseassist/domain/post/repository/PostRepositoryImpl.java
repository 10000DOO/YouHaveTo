package com.yht.exerciseassist.domain.post.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yht.exerciseassist.domain.post.Post;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.yht.exerciseassist.domain.post.QPost.post;


@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Post> findNotDeletedById(Long id, String role) {
        return Optional.ofNullable(queryFactory.selectFrom(post)
                .where(post.id.eq(id), memberRoleEq(role))
                .fetchOne());
    }

    private BooleanExpression memberRoleEq(String role) {
        return role.equals("USER") ? post.dateTime.canceledAt.isNull() : null;
    }
}
