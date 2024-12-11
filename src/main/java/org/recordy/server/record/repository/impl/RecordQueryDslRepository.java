package org.recordy.server.record.repository.impl;

import static org.recordy.server.bookmark.domain.QBookmarkEntity.bookmarkEntity;
import static org.recordy.server.place.domain.QPlaceEntity.placeEntity;
import static org.recordy.server.record.domain.QRecordEntity.recordEntity;
import static org.recordy.server.report.domain.QReport.report;
import static org.recordy.server.subscribe.domain.QSubscribeEntity.subscribeEntity;
import static org.recordy.server.user.domain.QUserEntity.userEntity;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.recordy.server.common.util.QueryDslUtils;
import org.recordy.server.record.controller.dto.response.RecordGetResponse;
import org.recordy.server.record.domain.RecordEntity;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class RecordQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public RecordEntity findById(Long id) {
        return jpaQueryFactory
                .selectFrom(recordEntity)
                .join(recordEntity.place, placeEntity).fetchJoin()
                .join(recordEntity.user, userEntity).fetchJoin()
                .where(recordEntity.id.eq(id))
                .fetchOne();
    }

    public Slice<RecordGetResponse> findAllByPlaceIdOrderByIdDesc(long placeId, long userId, Long cursor, int size) {
        List<RecordGetResponse> content = jpaQueryFactory
                .select(getRecordResponse(userId))
                .from(recordEntity)
                .join(recordEntity.user, userEntity)
                .join(recordEntity.place, placeEntity)
                .leftJoin(recordEntity, report.record)
                .where(
                        recordEntity.place.id.eq(placeId),
                        QueryDslUtils.ltCursorId(cursor, recordEntity.id),
                        report.reporter.id.ne(userId),
                        recordEntity.isBlocked.eq(false)
                )
                .groupBy(recordEntity.id)
                .orderBy(recordEntity.id.desc())
                .limit(size + 1)
                .fetch();

        return QueryDslUtils.getSlice(size, content);
    }

    public Slice<RecordGetResponse> findAllByUserIdOrderByIdDesc(long otherUserId, long userId, Long cursor, int size) {
        List<RecordGetResponse> content = jpaQueryFactory
                .select(getRecordResponse(userId))
                .from(recordEntity)
                .join(recordEntity.user, userEntity)
                .join(recordEntity.place, placeEntity)
                .leftJoin(recordEntity, report.record)
                .where(
                        userEntity.id.eq(otherUserId),
                        QueryDslUtils.ltCursorId(cursor, recordEntity.id),
                        report.reporter.id.ne(userId),
                        recordEntity.isBlocked.eq(false)
                )
                .groupBy(recordEntity.id)
                .orderBy(recordEntity.id.desc())
                .limit(size + 1)
                .fetch();

        return QueryDslUtils.getSlice(size, content);
    }

    public List<RecordGetResponse> findAllByIds(List<Long> ids, long userId) {
        return jpaQueryFactory
                .select(getRecordResponse(userId))
                .from(recordEntity)
                .join(recordEntity.user, userEntity)
                .join(recordEntity.place, placeEntity)
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
                .join(recordEntity.place, placeEntity)
                .join(recordEntity.bookmarks, bookmarkEntity)
                .leftJoin(recordEntity, report.record)
                .where(
                        bookmarkEntity.user.id.eq(userId),
                        QueryDslUtils.ltCursorId(cursor, recordEntity.id),
                        report.reporter.id.ne(userId),
                        recordEntity.isBlocked.eq(false)
                )
                .groupBy(recordEntity.id)
                .orderBy(recordEntity.id.desc())
                .limit(size + 1)
                .fetch();

        return QueryDslUtils.getSlice(size, content);
    }

    private ConstructorExpression<RecordGetResponse> getRecordResponse(long userId) {
        return Projections.constructor(RecordGetResponse.class,
                recordEntity.id,
                recordEntity.fileUrl,
                recordEntity.content,
                recordEntity.exhibitionName,
                recordEntity.place.id,
                recordEntity.place.name,
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
                .leftJoin(recordEntity, report.record)
                .where(
                        subscribeEntity.subscribingUser.id.eq(userId),
                        report.reporter.id.ne(userId),
                        recordEntity.isBlocked.eq(false)
                )
                .fetch();
    }

    public List<Long> findAllIds(long userId) {
        return jpaQueryFactory
                .select(recordEntity.id)
                .from(recordEntity)
                .leftJoin(recordEntity, report.record)
                .where(
                        report.reporter.id.ne(userId),
                        recordEntity.isBlocked.eq(false)
                )
                .fetch();
    }

    public Long countByUserIdAndCreatedAtBetween(long userId, LocalDateTime from, LocalDateTime to) {
        return jpaQueryFactory
                .select(recordEntity.id.count())
                .from(recordEntity)
                .where(
                        recordEntity.user.id.eq(userId),
                        recordEntity.createdAt.between(from, to)
                )
                .fetchFirst();
    }
}
