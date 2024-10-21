package org.recordy.server.user.repository.impl;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.recordy.server.common.util.QueryDslUtils;
import org.recordy.server.user.controller.dto.response.UserInfo;
import org.recordy.server.user.domain.UserEntity;
import org.recordy.server.user.domain.usecase.UserProfile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.recordy.server.record.domain.QRecordEntity.recordEntity;
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
        List<UserInfo> content = findSubscriptionUsers(subscribeEntity.subscribingUser.id.eq(userId), cursor, size);
        return new SliceImpl<>(content, PageRequest.ofSize(size), QueryDslUtils.hasNext(size, content));
    }

    public Slice<UserInfo> findFollowers(long userId, Long cursor, int size) {
        List<UserInfo> content = findSubscriptionUsers(subscribeEntity.subscribedUser.id.eq(userId), cursor, size);
        return new SliceImpl<>(content, PageRequest.ofSize(size), QueryDslUtils.hasNext(size, content));
    }

    private List<UserInfo> findSubscriptionUsers(BooleanExpression expression, Long cursor, int size) {
        return jpaQueryFactory
                .select(getUserInfo(expression))
                .from(userEntity)
                .join(userEntity.subscribers, subscribeEntity)
                .where(QueryDslUtils.ltCursorId(cursor, userEntity.id))
                .orderBy(subscribeEntity.id.desc())
                .limit(size + 1)
                .fetch();
    }

    private ConstructorExpression<UserInfo> getUserInfo(BooleanExpression expression) {
        return Projections.constructor(UserInfo.class,
                userEntity.id,
                userEntity.nickname,
                userEntity.profileImageUrl,
                JPAExpressions
                        .selectOne()
                        .from(subscribeEntity)
                        .where(expression)
                        .exists()
        );
    }

    public UserProfile findProfile(long targetUserId, long userId) {
        return jpaQueryFactory
                .select(getUserProfile(userId))
                .from(userEntity)
                .where(userEntity.id.eq(targetUserId))
                .fetchOne();
    }

    private ConstructorExpression<UserProfile> getUserProfile(long userId) {
        return Projections.constructor(UserProfile.class,
                userEntity.id,
                userEntity.nickname,
                userEntity.profileImageUrl,
                JPAExpressions
                        .select(recordEntity.count())
                        .from(recordEntity)
                        .where(recordEntity.user.eq(userEntity)),
                JPAExpressions
                        .select(subscribeEntity.count())
                        .from(subscribeEntity)
                        .where(subscribeEntity.subscribedUser.eq(userEntity)),
                JPAExpressions
                        .select(subscribeEntity.count())
                        .from(subscribeEntity)
                        .where(subscribeEntity.subscribingUser.eq(userEntity)),
                JPAExpressions
                        .select(new CaseBuilder()
                                .when(subscribeEntity.subscribingUser.id.eq(userId)
                                        .and(subscribeEntity.subscribedUser.eq(userEntity)))
                                .then(Expressions.constant(true))
                                .otherwise(Expressions.constant(false)))
                        .from(subscribeEntity)
                        .limit(1)
        );
    }
}
