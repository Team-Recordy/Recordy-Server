package org.recordy.server.user.repository.impl;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.recordy.server.common.util.QueryDslUtils;
import org.recordy.server.user.controller.dto.response.UserInfo;
import org.recordy.server.user.domain.UserEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.recordy.server.subscribe.domain.QSubscribeEntity.subscribeEntity;
import static org.recordy.server.user.domain.QUserEntity.userEntity;

@RequiredArgsConstructor
@Repository
public class UserQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public UserEntity findById(long userId) {
        return jpaQueryFactory
                .selectFrom(userEntity)
                .where(userEntity.id.eq(userId))
                .fetchOne();
    }

    public UserEntity findByPlatformId(String platformId) {
        return jpaQueryFactory
                .selectFrom(userEntity)
                .where(userEntity.platformId.eq(platformId))
                .fetchOne();
    }

    public Slice<UserInfo> findFollowings(long userId, Long cursor, int size) {
        List<UserInfo> content = jpaQueryFactory
                .select(getUserInfo(subscribeEntity.subscribingUser.id.eq(userId)))
                .from(userEntity)
                .join(userEntity.subscribings, subscribeEntity)
                .where(QueryDslUtils.ltCursorId(cursor, userEntity.id))
                .orderBy(subscribeEntity.id.desc())
                .limit(size + 1)
                .fetch();

        return new SliceImpl<>(content, PageRequest.ofSize(size), QueryDslUtils.hasNext(size, content));
    }

    public Slice<UserInfo> findFollowers(long userId, Long cursor, int size) {
        List<UserInfo> content = jpaQueryFactory
                .select(getUserInfo(subscribeEntity.subscribedUser.id.eq(userId)))
                .from(userEntity)
                .join(userEntity.subscribers, subscribeEntity)
                .where(QueryDslUtils.ltCursorId(cursor, userEntity.id))
                .orderBy(subscribeEntity.id.desc())
                .limit(size + 1)
                .fetch();

        return new SliceImpl<>(content, PageRequest.ofSize(size), QueryDslUtils.hasNext(size, content));
    }

    private ConstructorExpression<UserInfo> getUserInfo(BooleanExpression... expressions) {
        return Projections.constructor(UserInfo.class,
                userEntity.id,
                userEntity.nickname,
                userEntity.profileImageUrl,
                JPAExpressions
                        .selectOne()
                        .from(subscribeEntity)
                        .where(expressions)
                        .exists()
        );
    }
}
