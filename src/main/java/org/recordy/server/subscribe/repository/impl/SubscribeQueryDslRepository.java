package org.recordy.server.subscribe.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.recordy.server.common.util.QueryDslUtils;
import org.recordy.server.subscribe.domain.SubscribeEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.recordy.server.subscribe.domain.QSubscribeEntity.subscribeEntity;

@RequiredArgsConstructor
@Component
public class SubscribeQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Slice<SubscribeEntity> findAllBySubscribingUserId(long subscribingUserId, long cursor, Pageable pageable) {
        List<SubscribeEntity> subscribeEntities = jpaQueryFactory
                .selectFrom(subscribeEntity)
                .where(
                        QueryDslUtils.ltCursorId(cursor, subscribeEntity.id),
                        subscribeEntity.subscribingUser.id.eq(subscribingUserId)
                )
                .orderBy(subscribeEntity.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return new SliceImpl<>(subscribeEntities, pageable, QueryDslUtils.hasNext(pageable, subscribeEntities));
    }

    public long countSubscribingUsers(long subscribedUserId) {
        return jpaQueryFactory
                .select(subscribeEntity.id.count())
                .from(subscribeEntity)
                .where(subscribeEntity.subscribedUser.id.eq(subscribedUserId))
                .fetchOne();
    }

    public long countSubscribedUsers(long subscribingUserId) {
        return jpaQueryFactory
                .select(subscribeEntity.id.count())
                .from(subscribeEntity)
                .where(subscribeEntity.subscribingUser.id.eq(subscribingUserId))
                .fetchOne();
    }

    public boolean existsBySubscribingUserIdAndSubscribedUserId(long subscribingUserId, long subscribedUserId) {
        return jpaQueryFactory
                .select(subscribeEntity.id)
                .from(subscribeEntity)
                .where(
                        subscribeEntity.subscribingUser.id.eq(subscribingUserId),
                        subscribeEntity.subscribedUser.id.eq(subscribedUserId)
                )
                .fetchFirst() != null;
    }
}
