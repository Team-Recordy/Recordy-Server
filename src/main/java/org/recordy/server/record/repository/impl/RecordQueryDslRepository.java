package org.recordy.server.record.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.recordy.server.common.util.QueryDslUtils;
import org.recordy.server.record.domain.RecordEntity;
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

    public Slice<RecordEntity> findAllOrderByPopularity(Pageable pageable) {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        List<RecordEntity> recordEntities = jpaQueryFactory
                .selectFrom(recordEntity)
                .leftJoin(recordEntity.bookmarks, bookmarkEntity).on(bookmarkEntity.createdAt.after(sevenDaysAgo))
                .leftJoin(recordEntity.views, viewEntity).on(viewEntity.createdAt.after(sevenDaysAgo))
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
                .where(
                        QueryDslUtils.ltCursorId(cursor, recordEntity.id)
                )
                .orderBy(recordEntity.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(recordEntities, pageable, QueryDslUtils.hasNext(pageable, recordEntities));
    }

    public Slice<RecordEntity> findAllByUserIdOrderByIdDesc(long userId, Long cursor, Pageable pageable) {
        List<RecordEntity> recordEntities = jpaQueryFactory
                .selectFrom(recordEntity)
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

    public Slice<RecordEntity> findAllBySubscribingUserIdOrderByIdDesc(long userId, Long cursor, Pageable pageable) {
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
