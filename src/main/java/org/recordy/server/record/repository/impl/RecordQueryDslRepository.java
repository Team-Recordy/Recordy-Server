package org.recordy.server.record.repository.impl;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import org.recordy.server.common.util.QueryDslUtils;
import org.recordy.server.record.controller.dto.response.RecordGetResponse;
import org.recordy.server.record.domain.RecordEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.recordy.server.record.domain.QRecordEntity.recordEntity;
import static org.recordy.server.bookmark.domain.QBookmarkEntity.bookmarkEntity;
import static org.recordy.server.view.domain.QViewEntity.viewEntity;
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
                .leftJoin(recordEntity.bookmarks, bookmarkEntity)
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

    private ConstructorExpression<RecordGetResponse> getRecordResponse(long userId) {
        return Projections.constructor(RecordGetResponse.class,
                recordEntity.id,
                recordEntity.fileUrl,
                recordEntity.content,
                recordEntity.user.id,
                userEntity.nickname,
                bookmarkEntity.id.count(),
                new CaseBuilder()
                        .when(recordEntity.user.id.eq(userId))
                        .then(true)
                        .otherwise(false),
                new CaseBuilder()
                        .when(bookmarkEntity.user.id.eq(userId))
                        .then(true)
                        .otherwise(false)
        );
    }

    public Slice<RecordEntity> findAllOrderByPopularity(Pageable pageable) {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        List<RecordEntity> recordEntities = jpaQueryFactory
                .selectFrom(recordEntity)
                .leftJoin(recordEntity.bookmarks, bookmarkEntity).fetchJoin()
                .leftJoin(recordEntity.views, viewEntity).on(viewEntity.createdAt.after(sevenDaysAgo))
                .where(
                        bookmarkEntity.isNull()
                                .or(bookmarkEntity.createdAt.after(sevenDaysAgo))
                )
                .groupBy(recordEntity.id)
                .orderBy(bookmarkEntity.count().multiply(2).add(viewEntity.count()).desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(recordEntities, pageable, QueryDslUtils.hasNext(pageable, recordEntities));
    }

    public Slice<RecordEntity> findAllByIdAfterOrderByIdDesc(Long cursor, Pageable pageable) {
        List<RecordEntity> recordEntities = jpaQueryFactory
                .selectFrom(recordEntity)
                .leftJoin(recordEntity.bookmarks, bookmarkEntity).fetchJoin()
                .where(
                        QueryDslUtils.ltCursorId(cursor, recordEntity.id)
                )
                .orderBy(recordEntity.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(recordEntities, pageable, QueryDslUtils.hasNext(pageable, recordEntities));
    }

    public Slice<RecordEntity> findAllByUserIdOrderByIdDesc(long userId, Long cursor, Pageable pageable) {
        List<RecordEntity> recordEntities = jpaQueryFactory
                .selectFrom(recordEntity)
                .leftJoin(recordEntity.bookmarks, bookmarkEntity).fetchJoin()
                .join(recordEntity.user, userEntity).fetchJoin()
                .where(
                        QueryDslUtils.ltCursorId(cursor, recordEntity.id),
                        userEntity.id.eq(userId)
                )
                .orderBy(recordEntity.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(recordEntities, pageable, QueryDslUtils.hasNext(pageable, recordEntities));
    }

    public Slice<RecordEntity> findAllBySubscribingUserIdOrderByIdDesc(long userId, Long cursor, Pageable pageable) {
        List<RecordEntity> recordEntities = jpaQueryFactory
                .selectFrom(recordEntity)
                .leftJoin(recordEntity.bookmarks, bookmarkEntity).fetchJoin()
                .join(recordEntity.user, userEntity).fetchJoin()
                .join(userEntity.subscribers, subscribeEntity)
                .where(
                        QueryDslUtils.ltCursorId(cursor, recordEntity.id),
                        subscribeEntity.subscribingUser.id.eq(userId)
                )
                .orderBy(recordEntity.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(recordEntities, pageable, QueryDslUtils.hasNext(pageable, recordEntities));
    }

    public long countAllByUserId(long userId) {
        return jpaQueryFactory
                .select(recordEntity.id.count())
                .from(recordEntity)
                .where(recordEntity.user.id.eq(userId))
                .fetchOne();
    }

    public Optional<Long> findMaxId() {
        return Optional.ofNullable(jpaQueryFactory
                .select(recordEntity.id.max())
                .from(recordEntity)
                .fetchOne()
        );
    }
}
