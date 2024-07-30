package org.recordy.server.subscribe.repository.impl;

import org.recordy.server.subscribe.domain.SubscribeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface SubscribeJpaRepository extends JpaRepository<SubscribeEntity, Long> {

    void deleteAllBySubscribingUserIdAndSubscribedUserId(long subscribingUserId, long subscribedUserId);

    @Transactional
    void deleteAllBySubscribedUserIdOrSubscribingUserId(long subscribingUserId, long subscribedUserId);

}
