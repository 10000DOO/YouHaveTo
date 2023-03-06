package com.yht.exerciseassist.domain.post.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yht.exerciseassist.domain.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.Optional;

import static com.yht.exerciseassist.domain.post.PostType.*;
import static com.yht.exerciseassist.domain.post.QPost.post;
import static com.yht.exerciseassist.domain.post.WorkOutCategory.*;


@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Post> findByIdWithRole(Long id, String role) {
        return Optional.ofNullable(queryFactory.selectFrom(post)
                .where(post.id.eq(id), memberRoleEq(role))
                .fetchOne());
    }

    private BooleanExpression memberRoleEq(String role) {
        return role.equals("USER") ? post.dateTime.canceledAt.isNull() : null;
    }

    public Slice<Post> postAsSearchType(String role, List<String> postTypeList, List<String> WorkOutCategories, Pageable pageable) {
        List<Post> posts = queryFactory
                .selectFrom(post)
                .where(memberRoleEq(role), postTypeEq(postTypeList), workOutCategoriesEq(WorkOutCategories))
                .orderBy(post.dateTime.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = posts.size() > pageable.getPageSize();

        return new SliceImpl<>(posts, pageable, hasNext);
    }

    private BooleanExpression postTypeEq(List<String> postTypeList) {
        if (postTypeList == null || postTypeList.isEmpty()) {
            return null;
        } else {
            return Expressions.anyOf(postTypeList.stream().map(this::isSearchPostType).toArray(BooleanExpression[]::new));
        }
    }

    private BooleanExpression isSearchPostType(String postType) {
        return switch (postType) {
            case "Q_AND_A" -> post.postType.eq(Q_AND_A);
            case "KNOWLEDGE" -> post.postType.eq(KNOWLEDGE);
            case "SHOW_OFF" -> post.postType.eq(SHOW_OFF);
            case "COMPETITION" -> post.postType.eq(COMPETITION);
            case "FREE" -> post.postType.eq(FREE);
            default -> null;
        };
    }

    private BooleanExpression workOutCategoriesEq(List<String> workOutCategories) {
        if (workOutCategories == null || workOutCategories.isEmpty()) {
            return null;
        } else {
            return Expressions.anyOf(workOutCategories.stream().map(this::isSearchWorkOutCategories).toArray(BooleanExpression[]::new));
        }
    }

    private BooleanExpression isSearchWorkOutCategories(String workOutCategories) {
        return switch (workOutCategories) {
            case "HEALTH" -> post.workOutCategory.eq(HEALTH);
            case "PILATES" -> post.workOutCategory.eq(PILATES);
            case "YOGA" -> post.workOutCategory.eq(YOGA);
            case "JOGGING" -> post.workOutCategory.eq(JOGGING);
            case "ETC" -> post.workOutCategory.eq(ETC);
            default -> null;
        };
    }
}
