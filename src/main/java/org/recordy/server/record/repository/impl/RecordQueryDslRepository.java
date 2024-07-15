package org.recordy.server.record.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.recordy.server.common.util.QueryDslUtils;
import org.recordy.server.keyword.domain.KeywordEntity;
import org.recordy.server.record.domain.RecordEntity;
import org.recordy.server.subscribe.domain.QSubscribeEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.recordy.server.keyword.domain.QKeywordEntity.keywordEntity;
import static org.recordy.server.record.domain.QRecordEntity.recordEntity;
import static org.recordy.server.record.domain.QUploadEntity.uploadEntity;
import static org.recordy.server.record_stat.domain.QBookmarkEntity.bookmarkEntity;
import static org.recordy.server.record_stat.domain.QViewEntity.viewEntity;
import static org.recordy.server.subscribe.domain.QSubscribeEntity.subscribeEntity;
import static org.recordy.server.user.domain.QUserEntity.userEntity;

@RequiredArgsConstructor
@Repository
public class RecordQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Slice<RecordEntity> findAllOrderByPopularity(Pageable pageable) {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        List<RecordEntity> recordEntities = jpaQueryFactory
                .selectFrom(recordEntity)
                .leftJoin(recordEntity.bookmarks, bookmarkEntity)
                .leftJoin(recordEntity.views, viewEntity)
                .where(
                        bookmarkEntity.createdAt.after(sevenDaysAgo)
                                .or(viewEntity.createdAt.after(sevenDaysAgo))
                )
                .groupBy(recordEntity.id)
                .orderBy(bookmarkEntity.count().multiply(2).add(viewEntity.count()).desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(recordEntities, pageable, QueryDslUtils.hasNext(pageable, recordEntities));
    }

    public Slice<RecordEntity> findAllByKeywordsOrderByPopularity(List<KeywordEntity> keywords, Pageable pageable) {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        List<RecordEntity> recordEntities = jpaQueryFactory
                .selectFrom(recordEntity)
                .leftJoin(recordEntity.bookmarks, bookmarkEntity)
                .leftJoin(recordEntity.views, viewEntity)
                .join(recordEntity.uploads, uploadEntity)
                .join(uploadEntity.keyword, keywordEntity)
                .where(
                        bookmarkEntity.createdAt.after(sevenDaysAgo)
                                .or(viewEntity.createdAt.after(sevenDaysAgo)),
                        keywordEntity.in(keywords)
                )
                .groupBy(recordEntity.id)
                .orderBy(bookmarkEntity.count().multiply(2).add(viewEntity.count()).desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(recordEntities, pageable, QueryDslUtils.hasNext(pageable, recordEntities));
    }

    public Slice<RecordEntity> findAllByIdAfterAndKeywordsOrderByIdDesc(List<KeywordEntity> keywords, long cursor, Pageable pageable) {
        if (cursor == 0) {
            cursor = Long.MAX_VALUE;
        }

        List<RecordEntity> recordEntities = jpaQueryFactory
                .selectFrom(recordEntity)
                .join(recordEntity.uploads, uploadEntity)
                .join(uploadEntity.keyword, keywordEntity)
                .where(
                        QueryDslUtils.ltCursorId(cursor, recordEntity.id),
                        keywordEntity.in(keywords)
                )
                .orderBy(recordEntity.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(recordEntities, pageable, QueryDslUtils.hasNext(pageable, recordEntities));
    }

    public Slice<RecordEntity> findAllByIdAfterOrderByIdDesc(long cursor, Pageable pageable) {
        if (cursor == 0) {
            cursor = Long.MAX_VALUE;
        }

        List<RecordEntity> recordEntities = jpaQueryFactory
                .selectFrom(recordEntity)
                .where(
                        QueryDslUtils.ltCursorId(cursor, recordEntity.id)
                )
                .orderBy(recordEntity.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(recordEntities, pageable, QueryDslUtils.hasNext(pageable, recordEntities));
    }

    public Slice<RecordEntity> findAllByUserIdOrderByIdDesc(long userId, long cursor, Pageable pageable) {
        if (cursor == 0) {
            cursor = Long.MAX_VALUE;
        }

        List<RecordEntity> recordEntities = jpaQueryFactory
                .selectFrom((recordEntity))
                .join(recordEntity.user, userEntity)
                .where(
                        QueryDslUtils.ltCursorId(cursor, recordEntity.id),
                        userEntity.id.eq(userId)
                )
                .orderBy(recordEntity.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(recordEntities, pageable, QueryDslUtils.hasNext(pageable, recordEntities));
    }

    public Map<KeywordEntity, Long> countAllByUserIdGroupByKeyword(long userId) {
        List<Tuple> uploadResults = jpaQueryFactory
                .select(uploadEntity.keyword, uploadEntity.count())
                .from(uploadEntity)
                .join(uploadEntity.record, recordEntity)
                .where(recordEntity.user.id.eq(userId))
                .groupBy(uploadEntity.keyword)
                .fetch();

        List<Tuple> viewResults = jpaQueryFactory
                .select(uploadEntity.keyword, viewEntity.count())
                .from(viewEntity)
                .join(viewEntity.record, recordEntity)
                .join(recordEntity.uploads, uploadEntity)
                .where(viewEntity.user.id.eq(userId))
                .groupBy(uploadEntity.keyword)
                .fetch();

        List<Tuple> bookmarkResults = jpaQueryFactory
                .select(uploadEntity.keyword, bookmarkEntity.count())
                .from(bookmarkEntity)
                .join(bookmarkEntity.record, recordEntity)
                .join(recordEntity.uploads, uploadEntity)
                .where(bookmarkEntity.user.id.eq(userId))
                .groupBy(uploadEntity.keyword)
                .fetch();

        Map<KeywordEntity, Long> preference = uploadResults.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(uploadEntity.keyword),
                        tuple -> tuple.get(uploadEntity.count())));

        viewResults.forEach(tuple -> preference.merge(
                tuple.get(uploadEntity.keyword),
                tuple.get(viewEntity.count()),
                Long::sum));

        bookmarkResults.forEach(tuple -> preference.merge(
                tuple.get(uploadEntity.keyword),
                tuple.get(bookmarkEntity.count()),
                Long::sum));

        return preference;
    }

    public Slice<RecordEntity> findAllBySubscribingUserIdOrderByIdDesc(long userId, long cursor, Pageable pageable) {
        if (cursor == 0) {
            cursor = Long.MAX_VALUE;
        }

        List<RecordEntity> recordEntities = jpaQueryFactory
                .select(recordEntity)
                .from(subscribeEntity)
                .join(subscribeEntity.subscribedUser, userEntity)
                .join(userEntity.records, recordEntity)
                .where(
                        QueryDslUtils.ltCursorId(cursor, recordEntity.id),
                        subscribeEntity.subscribingUser.id.eq(userId)
                )
                .orderBy(recordEntity.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(recordEntities, pageable, QueryDslUtils.hasNext(pageable, recordEntities));
    }

    public Optional<Long> findMaxId() {
        return Optional.ofNullable(jpaQueryFactory
                .select(recordEntity.id.max())
                        .from(recordEntity)
                        .fetchOne()
        );
    }

}
