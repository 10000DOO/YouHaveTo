package com.yht.exerciseassist.admin.accuse.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yht.exerciseassist.domain.accuse.Accuse;
import com.yht.exerciseassist.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.yht.exerciseassist.domain.accuse.AccuseGetType.*;
import static com.yht.exerciseassist.domain.accuse.AccuseType.*;
import static com.yht.exerciseassist.domain.accuse.QAccuse.accuse;
import static com.yht.exerciseassist.domain.post.QPost.post;


@RequiredArgsConstructor
public class AdminAccuseRepositoryImpl implements AdminAccuseRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Accuse> accuseAsAccuseType(List<String> accuseTypeList, List<String> types, Pageable pageable) {
        List<Accuse> accuses = queryFactory
                .selectFrom(accuse)
                .where(accuseTypeEq(accuseTypeList), typeEq(types))
                .orderBy(accuse.dateTime.createdAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = accuses.size() > pageable.getPageSize();

        return new SliceImpl<>(accuses, pageable, hasNext);
    }

    private BooleanExpression typeEq(List<String> types) {
        if (types == null || types.isEmpty()) {
            return null;
        } else {
            BooleanExpression[] resultType = types.stream().map(this::isSearchType).toArray(BooleanExpression[]::new);
            if (Arrays.equals(resultType, new BooleanExpression[0])) {
                return null;
            } else {
                return Expressions.anyOf(resultType);
            }
        }
    }

    private BooleanExpression isSearchType(String type) {
        if (Objects.equals(type, "POST")) {
            return accuse.accuseGetType.eq(POST);
        } else if (Objects.equals(type, "COMMENT")) {
            return accuse.accuseGetType.eq(COMMENT);
        } else if (Objects.equals(type, "DONE")) {
            return accuse.accuseGetType.eq(DONE);
        } else {
            throw new IllegalArgumentException(ErrorCode.NO_MATCHED_TYPE.getMessage());
        }
    }

    private BooleanExpression accuseTypeEq(List<String> accuseTypeList) {
        if (accuseTypeList == null || accuseTypeList.isEmpty()) {
            return null;
        } else {
            BooleanExpression[] result = accuseTypeList.stream().map(this::isSearchAccuseType).toArray(BooleanExpression[]::new);
            if (Arrays.equals(result, new BooleanExpression[0])) {
                return null;
            } else {
                return Expressions.anyOf(result);
            }
        }
    }

    private BooleanExpression isSearchAccuseType(String accuseType) {
        if (Objects.equals(accuseType, "POLITICAL_CONTENT")) {
            return accuse.accuseType.eq(POLITICAL_CONTENT);
        } else if (Objects.equals(accuseType, "ABUSIVE_LANGUAGE_BELITTLE")) {
            return accuse.accuseType.eq(ABUSIVE_LANGUAGE_BELITTLE);
        } else if (Objects.equals(accuseType, "COMMERCIAL_ADVERTISING")) {
            return accuse.accuseType.eq(COMMERCIAL_ADVERTISING);
        } else if (Objects.equals(accuseType, "INADEQUATE_CONTENT")) {
            return accuse.accuseType.eq(INADEQUATE_CONTENT);
        } else if (Objects.equals(accuseType, "PORNOGRAPHY_CONTENT")) {
            return accuse.accuseType.eq(PORNOGRAPHY_CONTENT);
        } else if (accuseType.isEmpty()) {
            return null;
        } else {
            throw new IllegalArgumentException(ErrorCode.NO_MATCHED_ACCUSE_TYPE.getMessage());
        }
    }
}
