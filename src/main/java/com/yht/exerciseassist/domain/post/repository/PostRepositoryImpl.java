package com.yht.exerciseassist.domain.post.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yht.exerciseassist.domain.post.Post;
import com.yht.exerciseassist.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.Arrays;
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

    public Slice<Post> postAsSearchType(String role, List<String> postTypeList, List<String> WorkOutCategories, String username, Pageable pageable) {
        List<Post> posts = queryFactory
                .selectFrom(post)
                .where(memberRoleEq(role), postTypeEq(postTypeList), workOutCategoriesEq(WorkOutCategories), usernameEq(username))
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
            BooleanExpression[] result = postTypeList.stream().map(this::isSearchPostType).toArray(BooleanExpression[]::new);
            if (Arrays.equals(result, new BooleanExpression[0])) {
                return null;
            } else {
                return Expressions.anyOf(result);
            }
        }
    }

    private BooleanExpression isSearchPostType(String postType) {
        if (postType.equals("Q_AND_A")) {
            return post.postType.eq(Q_AND_A);
        } else if (postType.equals("KNOWLEDGE")) {
            return post.postType.eq(KNOWLEDGE);
        } else if (postType.equals("SHOW_OFF")) {
            return post.postType.eq(SHOW_OFF);
        } else if (postType.equals("COMPETITION")) {
            return post.postType.eq(COMPETITION);
        } else if (postType.equals("FREE")) {
            return post.postType.eq(FREE);
        } else if (postType.isEmpty()) {
            return null;
        } else {
            throw new IllegalArgumentException(ErrorCode.NO_MATCHED_POST_TYPE.getMessage());
        }
    }

    private BooleanExpression workOutCategoriesEq(List<String> workOutCategories) {
        if (workOutCategories == null || workOutCategories.isEmpty()) {
            return null;
        } else {
            BooleanExpression[] result = workOutCategories.stream().map(this::isSearchWorkOutCategories).toArray(BooleanExpression[]::new);
            if (Arrays.equals(result, new BooleanExpression[0])) {
                return null;
            } else {
                return Expressions.anyOf(result);
            }
        }
    }

    private BooleanExpression isSearchWorkOutCategories(String workOutCategories) {
        if (workOutCategories.equals("HEALTH")) {
            return post.workOutCategory.eq(HEALTH);
        } else if (workOutCategories.equals("PILATES")) {
            return post.workOutCategory.eq(PILATES);
        } else if (workOutCategories.equals("YOGA")) {
            return post.workOutCategory.eq(YOGA);
        } else if (workOutCategories.equals("JOGGING")) {
            return post.workOutCategory.eq(JOGGING);
        } else if (workOutCategories.equals("ETC")) {
            return post.workOutCategory.eq(ETC);
        } else if (workOutCategories.isEmpty()) {
            return null;
        } else {
            throw new IllegalArgumentException(ErrorCode.NO_MATCHED_EXERCISE_CATEGORY.getMessage());
        }
    }

    private BooleanExpression usernameEq(String username) {
        return username == null ? null : post.postWriter.username.eq(username);
    }
}
