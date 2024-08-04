package org.recordy.server.subscribe.repository.impl;

import lombok.RequiredArgsConstructor;
import org.recordy.server.subscribe.domain.Subscribe;
import org.recordy.server.subscribe.domain.SubscribeEntity;
import org.recordy.server.subscribe.repository.SubscribeRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Repository
public class SubscribeRepositoryImpl implements SubscribeRepository {

    private final SubscribeJpaRepository subscribeJpaRepository;
    private final SubscribeQueryDslRepository subscribeQueryDslRepository;

    @Transactional
    @Override
    public void save(Subscribe subscribe) {
        subscribeJpaRepository.save(SubscribeEntity.from(subscribe));
    }

    @Transactional
    @Override
    public void delete(long subscribingUserId, long subscribedUserId) {
        subscribeJpaRepository.deleteAllBySubscribingUserIdAndSubscribedUserId(subscribingUserId, subscribedUserId);
    }

    @Transactional
    @Override
    public void deleteByUserId(long userId) {
        subscribeJpaRepository.deleteAllBySubscribedUserIdOrSubscribingUserId(userId, userId);
    }


    @Override
    public Slice<Subscribe> findAllBySubscribingUserId(long subscribingUserId, Long cursor, Pageable pageable) {
        return subscribeQueryDslRepository.findAllBySubscribingUserId(subscribingUserId, cursor, pageable)
                .map(SubscribeEntity::toDomain);
    }

    @Override
    public Slice<Subscribe> findAllBySubscribedUserId(long subscribedUserId, Long cursor, Pageable pageable) {
        return subscribeQueryDslRepository.findAllBySubscribedUserId(subscribedUserId, cursor, pageable)
                .map(SubscribeEntity::toDomain);
    }

    @Override
    public boolean existsBySubscribingUserIdAndSubscribedUserId(long subscribingUserId, long subscribedUserId) {
        return subscribeQueryDslRepository.existsBySubscribingUserIdAndSubscribedUserId(subscribingUserId, subscribedUserId);
    }

    @Override
    public long countSubscribingUsers(long subscribedUserId) {
        return subscribeQueryDslRepository.countSubscribingUsers(subscribedUserId);
    }

    @Override
    public long countSubscribedUsers(long subscribingUserId) {
        return subscribeQueryDslRepository.countSubscribedUsers(subscribingUserId);
    }
}
