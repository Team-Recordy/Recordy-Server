package org.recordy.server.record.repository.impl;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import org.recordy.server.common.util.QueryDslUtils;
import org.recordy.server.record.controller.dto.response.RecordGetResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.recordy.server.record.domain.QRecordEntity.recordEntity;
import static org.recordy.server.bookmark.domain.QBookmarkEntity.bookmarkEntity;
import static org.recordy.server.subscribe.domain.QSubscribeEntity.subscribeEntity;
import static org.recordy.server.user.domain.QUserEntity.userEntity;

@RequiredArgsConstructor
@Repository
public class RecordQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Slice<RecordGetResponse> findAllByPlaceIdOrderByIdDesc(long placeId, long userId, Long cursor, int size) {
        List<RecordGetResponse> content = jpaQueryFactory
                .select(getRecordResponse(userId))
                .from(recordEntity)
                .join(recordEntity.user, userEntity)
                .where(
                        recordEntity.place.id.eq(placeId),
                        QueryDslUtils.ltCursorId(cursor, recordEntity.id)
                )
                .groupBy(recordEntity.id)
                .orderBy(recordEntity.id.desc())
                .limit(size + 1)
                .fetch();

        return new SliceImpl<>(content, PageRequest.ofSize(size), QueryDslUtils.hasNext(size, content));
    }

    public Slice<RecordGetResponse> findAllByUserIdOrderByIdDesc(long otherUserId, long userId, Long cursor, int size) {
        List<RecordGetResponse> content = jpaQueryFactory
                .select(getRecordResponse(userId))
                .from(recordEntity)
                .join(recordEntity.user, userEntity)
                .where(
                        userEntity.id.eq(otherUserId),
                        QueryDslUtils.ltCursorId(cursor, recordEntity.id)
                )
                .groupBy(recordEntity.id)
                .orderBy(recordEntity.id.desc())
                .limit(size + 1)
                .fetch();

        return new SliceImpl<>(content, PageRequest.ofSize(size), QueryDslUtils.hasNext(size, content));
    }

    public List<RecordGetResponse> findAllByIds(List<Long> ids, long userId) {
        return jpaQueryFactory
                .select(getRecordResponse(userId))
                .from(recordEntity)
                .join(recordEntity.user, userEntity)
                .where(recordEntity.id.in(ids))
                .groupBy(recordEntity.id)
                .orderBy(recordEntity.id.desc())
                .fetch();
    }

    public Slice<RecordGetResponse> findAllByBookmarkOrderByIdDesc(long userId, Long cursor, int size) {
        List<RecordGetResponse> content = jpaQueryFactory
                .select(getRecordResponse(userId))
                .from(recordEntity)
                .join(recordEntity.user, userEntity)
                .where(
                        bookmarkEntity.user.id.eq(userId),
                        QueryDslUtils.ltCursorId(cursor, recordEntity.id)
                )
                .groupBy(recordEntity.id)
                .orderBy(recordEntity.id.desc())
                .limit(size + 1)
                .fetch();

        return new SliceImpl<>(content, PageRequest.ofSize(size), QueryDslUtils.hasNext(size, content));
    }

    private ConstructorExpression<RecordGetResponse> getRecordResponse(long userId) {
        return Projections.constructor(RecordGetResponse.class,
                recordEntity.id,
                recordEntity.fileUrl,
                recordEntity.content,
                recordEntity.user.id,
                recordEntity.user.nickname,
                JPAExpressions
                        .select(bookmarkEntity.count())
                        .from(bookmarkEntity)
                        .where(bookmarkEntity.record.eq(recordEntity)),
                recordEntity.user.id.eq(userId),
                JPAExpressions
                        .selectOne()
                        .from(bookmarkEntity)
                        .where(bookmarkEntity.record.eq(recordEntity)
                                .and(bookmarkEntity.user.id.eq(userId)))
                        .exists()
        );
    }

    public List<Long> findAllIdsBySubscribingUserId(long userId) {
        return jpaQueryFactory
                .select(recordEntity.id)
                .from(recordEntity)
                .join(recordEntity.user, userEntity)
                .join(userEntity.subscribers, subscribeEntity)
                .where(subscribeEntity.subscribingUser.id.eq(userId))
                .fetch();
    }

    public List<Long> findAllIds() {
        return jpaQueryFactory
                .select(recordEntity.id)
                .from(recordEntity)
                .fetch();
    }
}
